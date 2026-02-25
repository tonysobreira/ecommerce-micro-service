package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.email.EmailClient;
import com.example.orderservice.domain.*;
import com.example.orderservice.dto.request.*;
import com.example.orderservice.dto.response.*;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.dto.email.OrderStatusEmailRequest;
import com.example.orderservice.errors.BadRequestException;
import com.example.orderservice.errors.ForbiddenException;
import com.example.orderservice.errors.NotFoundException;
import com.example.orderservice.repo.OrderItemRepository;
import com.example.orderservice.repo.OrderRepository;
import com.example.orderservice.repo.OrderStatusHistoryRepository;
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
	private final OrderRepository orders;
	private final OrderItemRepository items;
	private final OrderStatusHistoryRepository history;
	private final EmailClient emailClient;
	private final OrderMapper orderMapper;

	public OrderService(ProductClient productClient, OrderRepository orders, OrderItemRepository items,
			OrderStatusHistoryRepository history, EmailClient emailClient, OrderMapper orderMapper) {
		this.productClient = productClient;
		this.orders = orders;
		this.items = items;
		this.history = history;
		this.emailClient = emailClient;
		this.orderMapper = orderMapper;
	}

	/**
	 * Saga-ish flow: 1) Quote products (exist/active/price/stock) 2) Reserve stock
	 * (atomic on product-service side) 3) Persist order/items/history (local DB tx)
	 * 4) If DB tx fails after reserve, best-effort release.
	 */
	public OrderResponse create(UUID userId, String email, CreateOrderRequest req) {
		if (req.getItems() == null || req.getItems().isEmpty()) {
			throw new BadRequestException("Order must contain items");
		}

		// Build unique product list
		List<UUID> productIds = req.getItems().stream().map(CreateOrderItem::getProductId).distinct().toList();
		String idsCsv = productIds.stream().map(UUID::toString).collect(Collectors.joining(","));

		QuoteResponse quote = productClient.quote(idsCsv);
		Map<UUID, QuoteItemResponse> quoteMap = quote.getItems().stream()
				.collect(Collectors.toMap(QuoteItemResponse::getProductId, Function.identity(), (a, b) -> a));

		// Validate and compute totals with authoritative prices
		String currency = null;
		long subtotal = 0;

		for (CreateOrderItem i : req.getItems()) {
			QuoteItemResponse qi = quoteMap.get(i.getProductId());

			if (qi == null || !qi.isExists()) {
				throw new BadRequestException("Product not found: " + i.getProductId());
			}

			if (!qi.isActive()) {
				throw new BadRequestException("Product inactive: " + i.getProductId());
			}

			if (qi.getStock() < i.getQuantity()) {
				throw new BadRequestException("Insufficient stock: " + i.getProductId());
			}

			if (currency == null) {
				currency = qi.getCurrency();
			}

			if (!Objects.equals(currency, qi.getCurrency())) {
				throw new BadRequestException("Mixed currencies not supported");
			}

			subtotal += qi.getPriceCents() * (long) i.getQuantity();
		}

		if (currency == null) {
			throw new BadRequestException("Unable to determine currency");
		}

		// Reserve stock before local DB write
		StockReserveRequest reserveReq = new StockReserveRequest(
				req.getItems().stream().map(i -> new StockReserveItem(i.getProductId(), i.getQuantity())).toList());

		productClient.reserve(reserveReq);

		try {
			return persistOrder(userId, email, req, quoteMap, currency, subtotal);
		} catch (RuntimeException ex) {
			// Best-effort compensation: release
			try {
				productClient.release(new StockReleaseRequest(reserveReq.getItems()));
			} catch (Exception ignore) {
				// log in real-world
			}
			throw ex;
		}
	}

	@Transactional
	protected OrderResponse persistOrder(UUID userId, String email, CreateOrderRequest req, Map<UUID, QuoteItemResponse> quoteMap,
			String currency, long subtotal) {

		long shipping = PricingCalculator.shippingCents(subtotal);
		long total = subtotal + shipping;

		PaymentMethod pm;
		try {
			pm = PaymentMethod.valueOf(req.getPaymentMethod().trim().toUpperCase(Locale.ROOT));
		} catch (Exception e) {
			throw new BadRequestException("Unsupported paymentMethod: " + req.getPaymentMethod());
		}

		Instant now = Instant.now();
		UUID orderId = UUID.randomUUID();

		AddressDto a = req.getShippingAddress();

		Order order = new Order(orderId, userId, email, OrderStatus.CREATED, pm, a.getLine1(), a.getLine2(), a.getCity(),
				a.getState(), a.getZip(), a.getCountry(), currency, subtotal, shipping, total, now, now);

		orders.save(order);

		List<OrderItem> itemEntities = new ArrayList<>();
		for (CreateOrderItem i : req.getItems()) {
			QuoteItemResponse qi = quoteMap.get(i.getProductId());
			OrderItem oi = new OrderItem(UUID.randomUUID(), orderId, i.getProductId(), i.getQuantity(),
					qi.getPriceCents(), currency, now);
			itemEntities.add(oi);
		}
		items.saveAll(itemEntities);

		history.save(new OrderStatusHistory(UUID.randomUUID(), orderId, OrderStatus.CREATED, userId, now));
		notifyOrderStatus(order);

		return orderMapper.toResponse(order, itemEntities, history.findByOrderIdOrderByChangedAtAsc(orderId));
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> listMy(UUID userId) {
		List<Order> my = orders.findByUserIdOrderByCreatedAtDesc(userId);
		return my.stream().map(o -> {
			List<OrderItem> its = items.findByOrderId(o.getId());
			List<OrderStatusHistory> hist = history.findByOrderIdOrderByChangedAtAsc(o.getId());
			return orderMapper.toResponse(o, its, hist);
		}).toList();
	}

	@Transactional(readOnly = true)
	public OrderResponse get(UUID requester, boolean isAdmin, UUID orderId) {
		Order o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

		if (!isAdmin && !o.getUserId().equals(requester)) {
			throw new ForbiddenException("Not allowed");
		}

		List<OrderItem> its = items.findByOrderId(orderId);
		List<OrderStatusHistory> hist = history.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, its, hist);
	}

	@Transactional
	public OrderResponse updateStatus(UUID adminId, UUID orderId, String newStatusRaw) {
		Order o = orders.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

		OrderStatus ns;
		try {
			ns = OrderStatus.valueOf(newStatusRaw.trim().toUpperCase(Locale.ROOT));
		} catch (Exception e) {
			throw new BadRequestException("Invalid status: " + newStatusRaw);
		}

		o.setStatus(ns);
		orders.save(o);

		history.save(new OrderStatusHistory(UUID.randomUUID(), orderId, ns, adminId, Instant.now()));
		notifyOrderStatus(o);

		List<OrderItem> its = items.findByOrderId(orderId);
		List<OrderStatusHistory> hist = history.findByOrderIdOrderByChangedAtAsc(orderId);
		return orderMapper.toResponse(o, its, hist);
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