package com.example.orderservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderservice.client.InternalInventoryClient;
import com.example.orderservice.client.InternalUserClient;
import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.dto.request.CreateOrderItemRequest;
import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.request.CreatePaymentRequest;
import com.example.orderservice.dto.request.StockReleaseRequest;
import com.example.orderservice.dto.request.StockReserveItem;
import com.example.orderservice.dto.request.StockReserveRequest;
import com.example.orderservice.dto.request.UpdateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.QuoteItemResponse;
import com.example.orderservice.dto.response.QuoteResponse;
import com.example.orderservice.dto.response.UserAddressResponse;
import com.example.orderservice.exception.BadRequestException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.messaging.EmailEventPublisher;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.model.OrderStatusHistory;
import com.example.orderservice.model.PaymentMethod;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderStatusHistoryRepository;
import com.example.orderservice.security.UserPrincipal;
import com.example.orderservice.util.MoneyUtils;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orderRepository;

	private final OrderItemRepository orderItemRepository;

	private final OrderStatusHistoryRepository orderStatusHistoryRepository;

	private final PaymentClient paymentClient;

	private final InternalInventoryClient internalInventoryClient;

	private final InternalUserClient internalUserClient;

	private final OrderMapper orderMapper;

	private final EmailEventPublisher emailEventPublisher;

	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			OrderStatusHistoryRepository orderStatusHistoryRepository, PaymentClient paymentClient,
			InternalInventoryClient internalInventoryClient, InternalUserClient internalUserClient,
			OrderMapper orderMapper, EmailEventPublisher emailEventPublisher) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.orderStatusHistoryRepository = orderStatusHistoryRepository;
		this.paymentClient = paymentClient;
		this.internalInventoryClient = internalInventoryClient;
		this.internalUserClient = internalUserClient;
		this.orderMapper = orderMapper;
		this.emailEventPublisher = emailEventPublisher;
	}

	/**
	 * Saga-ish flow: 1) Quote products (exist/active/price/stock) 2) Reserve stock
	 * (atomic on product-service side) 3) Persist order/items/history (local DB tx)
	 * 4) If DB tx fails after reserve, best-effort release.
	 */
	public OrderResponse create(UUID userId, String email, CreateOrderRequest req) {
		if (req.items() == null || req.items().isEmpty()) {
			throw new BadRequestException("Order must contain items");
		}

		// Build unique product list
		List<UUID> productIds = req.items().stream().map(CreateOrderItemRequest::productId).distinct().toList();
		String idsCsv = productIds.stream().map(UUID::toString).collect(Collectors.joining(","));

		QuoteResponse quote = internalInventoryClient.quote(idsCsv);
		Map<UUID, QuoteItemResponse> quoteMap = quote.items().stream()
				.collect(Collectors.toMap(QuoteItemResponse::productId, Function.identity(), (a, b) -> a));

		// Validate and compute totals with authoritative prices
		String currency = null;
		BigDecimal subtotal = BigDecimal.ZERO;

		for (CreateOrderItemRequest i : req.items()) {
			QuoteItemResponse qi = quoteMap.get(i.productId());

			if (qi == null || !qi.exists()) {
				throw new BadRequestException("Product not found: " + i.productId());
			}

			if (!qi.active()) {
				throw new BadRequestException("Product inactive: " + i.productId());
			}

			if (qi.availableQuantity() < i.quantity()) {
				throw new BadRequestException("Insufficient stock: " + i.productId());
			}

			if (currency == null) {
				currency = qi.currency();
			}

			if (!Objects.equals(currency, qi.currency())) {
				throw new BadRequestException("Mixed currencies not supported");
			}

			subtotal = subtotal.add(qi.priceCents().multiply(BigDecimal.valueOf(i.quantity())));
		}

		if (currency == null) {
			throw new BadRequestException("Unable to determine currency");
		}

		// Reserve stock before local DB write
		UUID orderReservationId = UUID.randomUUID();
		StockReserveRequest reserveReq = new StockReserveRequest(orderReservationId,
				req.items().stream().map(i -> new StockReserveItem(i.productId(), i.quantity())).toList());

		internalInventoryClient.reserve(reserveReq);

		try {
			return persistOrder(userId, email, req, quoteMap, currency, subtotal);
		} catch (RuntimeException ex) {
			// Best-effort compensation: release
			try {
				internalInventoryClient.release(new StockReleaseRequest(orderReservationId, reserveReq.items()));
			} catch (Exception ignore) {
				// log in real-world
				log.warn("Create order exception: {}", ex);
			}
			throw ex;
		}
	}

	@Transactional
	protected OrderResponse persistOrder(UUID userId, String email, CreateOrderRequest req,
			Map<UUID, QuoteItemResponse> quoteMap, String currency, BigDecimal subtotal) {

		BigDecimal shipping = PricingCalculator.shippingCents(subtotal);
		BigDecimal total = subtotal.add(shipping);

		PaymentMethod pm;
		try {
			pm = PaymentMethod.valueOf(req.paymentMethod().trim().toUpperCase(Locale.ROOT));
		} catch (Exception e) {
			throw new BadRequestException("Unsupported paymentMethod: " + req.paymentMethod());
		}

		UserAddressResponse a = internalUserClient.findByUserIdAndUserProfileId(userId, req.userAddressId());

		Order order = new Order(userId, email, OrderStatus.CREATED, pm, a.line1(), a.line2(), a.city(), a.state(),
				a.zip(), a.country(), currency, subtotal, shipping, total);

		orderRepository.save(order);

		List<OrderItem> itemEntities = new ArrayList<>();
		for (CreateOrderItemRequest i : req.items()) {
			QuoteItemResponse qi = quoteMap.get(i.productId());
			OrderItem oi = new OrderItem(order.getId(), i.productId(), i.quantity(), qi.priceCents(), currency);
			itemEntities.add(oi);
		}
		orderItemRepository.saveAll(itemEntities);

		orderStatusHistoryRepository.save(new OrderStatusHistory(order.getId(), OrderStatus.CREATED, userId));

		paymentClient.createPendingPayment(
				new CreatePaymentRequest(order.getId(), userId, MoneyUtils.centsToAmount(total), pm));

		notifyOrderStatus(order);

		return orderMapper.toResponse(order, itemEntities,
				orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(order.getId()));
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> findByUserId(UUID userId) {
		List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
		return orders.stream().map(o -> {
			List<OrderItem> items = orderItemRepository.findByOrderId(o.getId());
			List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(o.getId());
			return orderMapper.toResponse(o, items, history);
		}).toList();
	}

	@Transactional(readOnly = true)
	public OrderResponse findByOrderId(UUID orderId, UserPrincipal principal) {
		Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
		assertOwnerOrAdmin(principal, o.getUserId());
		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, items, history);
	}

	@Transactional
	public OrderResponse update(UUID orderId, UpdateOrderRequest req) {
		Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
		OrderStatus previousStatus = o.statusEnum();

		if (previousStatus != req.status()) {
			syncInventoryByStatusTransition(o, req.status());
		}

		o.setStatus(req.status());

		orderRepository.save(o);

		orderStatusHistoryRepository.save(new OrderStatusHistory(orderId, req.status(), o.getUserId()));

		notifyOrderStatus(o);

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, items, history);
	}

	private void syncInventoryByStatusTransition(Order order, OrderStatus newStatus) {
		List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
		if (items.isEmpty()) {
			return;
		}

		StockReleaseRequest request = new StockReleaseRequest(order.getId(),
				items.stream().map(i -> new StockReserveItem(i.getProductId(), i.getQuantity())).toList());

		if (newStatus == OrderStatus.PAID) {
			internalInventoryClient.commit(request);
			return;
		}

		if (newStatus == OrderStatus.CANCELLED) {
			internalInventoryClient.release(request);
		}
	}

	private void assertOwnerOrAdmin(UserPrincipal principal, UUID userId) {
		if (!principal.isAdmin() && !principal.getUserId().equals(userId)) {
			throw new AccessDeniedException("You are not allowed.");
		}
	}

	private void notifyOrderStatus(Order order) {
		try {
			emailEventPublisher.publishOrderStatus(order.getCustomerEmail(), order.getId(), order.getStatus(),
					order.getCurrency(), order.getTotalCents());
		} catch (Exception ex) {
			log.warn("Unable to send order status email for order {}", order.getId(), ex);
		}
	}

	// INTERNAL
	@Transactional(readOnly = true)
	public OrderResponse findByOrderIdInternal(UUID orderId) {
		Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, items, history);
	}

}
