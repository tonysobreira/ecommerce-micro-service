package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.email.EmailClient;
import com.example.orderservice.model.*;
import com.example.orderservice.dto.request.*;
import com.example.orderservice.dto.response.*;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.dto.email.OrderStatusEmailRequest;
import com.example.orderservice.exception.BadRequestException;
import com.example.orderservice.exception.ForbiddenException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderStatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final ProductClient productClient;

	private final OrderRepository orderRepository;

	private final OrderItemRepository orderItemRepository;

	private final OrderStatusHistoryRepository orderStatusHistoryRepository;

	private final EmailClient emailClient;

	private final OrderMapper orderMapper;

	public OrderService(ProductClient productClient, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, OrderStatusHistoryRepository orderStatusHistoryRepository,
			EmailClient emailClient, OrderMapper orderMapper) {
		this.productClient = productClient;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.orderStatusHistoryRepository = orderStatusHistoryRepository;
		this.emailClient = emailClient;
		this.orderMapper = orderMapper;
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

		QuoteResponse quote = productClient.quote(idsCsv);
		Map<UUID, QuoteItemResponse> quoteMap = quote.items().stream()
				.collect(Collectors.toMap(QuoteItemResponse::productId, Function.identity(), (a, b) -> a));

		// Validate and compute totals with authoritative prices
		String currency = null;
		long subtotal = 0;

		for (CreateOrderItemRequest i : req.items()) {
			QuoteItemResponse qi = quoteMap.get(i.productId());

			if (qi == null || !qi.exists()) {
				throw new BadRequestException("Product not found: " + i.productId());
			}

			if (!qi.active()) {
				throw new BadRequestException("Product inactive: " + i.productId());
			}

			if (qi.stock() < i.quantity()) {
				throw new BadRequestException("Insufficient stock: " + i.productId());
			}

			if (currency == null) {
				currency = qi.currency();
			}

			if (!Objects.equals(currency, qi.currency())) {
				throw new BadRequestException("Mixed currencies not supported");
			}

			subtotal += qi.priceCents() * (long) i.quantity();
		}

		if (currency == null) {
			throw new BadRequestException("Unable to determine currency");
		}

		// Reserve stock before local DB write
		StockReserveRequest reserveReq = new StockReserveRequest(
				req.items().stream().map(i -> new StockReserveItem(i.productId(), i.quantity())).toList());

		productClient.reserve(reserveReq);

		try {
			return persistOrder(userId, email, req, quoteMap, currency, subtotal);
		} catch (RuntimeException ex) {
			// Best-effort compensation: release
			try {
				productClient.release(new StockReleaseRequest(reserveReq.items()));
			} catch (Exception ignore) {
				// log in real-world
			}
			throw ex;
		}
	}

	@Transactional
	protected OrderResponse persistOrder(UUID userId, String email, CreateOrderRequest req,
			Map<UUID, QuoteItemResponse> quoteMap, String currency, long subtotal) {

		long shipping = PricingCalculator.shippingCents(subtotal);
		long total = subtotal + shipping;

		PaymentMethod pm;
		try {
			pm = PaymentMethod.valueOf(req.paymentMethod().trim().toUpperCase(Locale.ROOT));
		} catch (Exception e) {
			throw new BadRequestException("Unsupported paymentMethod: " + req.paymentMethod());
		}

		Instant now = Instant.now();
		UUID orderId = UUID.randomUUID();

		AddressRequest a = req.shippingAddress();

		Order order = new Order(orderId, userId, email, OrderStatus.CREATED, pm, a.line1(), a.line2(), a.city(),
				a.state(), a.zip(), a.country(), currency, subtotal, shipping, total, now, now);

		orderRepository.save(order);

		List<OrderItem> itemEntities = new ArrayList<>();
		for (CreateOrderItemRequest i : req.items()) {
			QuoteItemResponse qi = quoteMap.get(i.productId());
			OrderItem oi = new OrderItem(UUID.randomUUID(), orderId, i.productId(), i.quantity(), qi.priceCents(),
					currency, now);
			itemEntities.add(oi);
		}
		orderItemRepository.saveAll(itemEntities);

		orderStatusHistoryRepository
				.save(new OrderStatusHistory(UUID.randomUUID(), orderId, OrderStatus.CREATED, userId, now));
		notifyOrderStatus(order);

		return orderMapper.toResponse(order, itemEntities,
				orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId));
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> listMy(UUID userId) {
		List<Order> my = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
		return my.stream().map(o -> {
			List<OrderItem> items = orderItemRepository.findByOrderId(o.getId());
			List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(o.getId());
			return orderMapper.toResponse(o, items, history);
		}).toList();
	}

	@Transactional(readOnly = true)
	public OrderResponse get(UUID requester, boolean isAdmin, UUID orderId) {
		Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

		if (!isAdmin && !o.getUserId().equals(requester)) {
			throw new ForbiddenException("Not allowed");
		}

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, items, history);
	}

	@Transactional
	public OrderResponse updateStatus(UUID adminId, UUID orderId, String newStatusRaw) {
		Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

		OrderStatus ns;

		try {
			ns = OrderStatus.valueOf(newStatusRaw.trim().toUpperCase(Locale.ROOT));
		} catch (Exception e) {
			throw new BadRequestException("Invalid status: " + newStatusRaw);
		}

		o.setStatus(ns);
		orderRepository.save(o);

		orderStatusHistoryRepository
				.save(new OrderStatusHistory(UUID.randomUUID(), orderId, ns, adminId, Instant.now()));
		notifyOrderStatus(o);

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, items, history);
	}

	private void notifyOrderStatus(Order order) {
		try {
			emailClient.sendOrderStatus(new OrderStatusEmailRequest(order.getCustomerEmail(), order.getId(),
					order.getStatus(), order.getCurrency(), order.getTotalCents()));
		} catch (Exception ex) {
			log.warn("Unable to send order status email for order {}", order.getId(), ex);
		}
	}

}