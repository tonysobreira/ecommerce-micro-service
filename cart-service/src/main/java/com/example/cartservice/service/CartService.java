package com.example.cartservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartservice.client.OrderClient;
import com.example.cartservice.client.ProductClient;
import com.example.cartservice.dto.request.AddCartItemRequest;
import com.example.cartservice.dto.request.CheckoutRequest;
import com.example.cartservice.dto.request.CreateOrderItemRequest;
import com.example.cartservice.dto.request.CreateOrderRequest;
import com.example.cartservice.dto.request.UpdateCartItemRequest;
import com.example.cartservice.dto.response.CartItemResponse;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.dto.response.CheckoutResponse;
import com.example.cartservice.dto.response.OrderResponse;
import com.example.cartservice.dto.response.ProductQuoteItemResponse;
import com.example.cartservice.dto.response.ProductQuoteResponse;
import com.example.cartservice.exception.BadRequestException;
import com.example.cartservice.exception.NotFoundException;
import com.example.cartservice.model.CartDocument;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repository.CartRepository;

@Service
public class CartService {

	private static final long CART_TTL_DAYS = 7;

	private final CartRepository repository;
	private final ProductClient productClient;
	private final OrderClient orderClient;

	public CartService(CartRepository repository, ProductClient productClient, OrderClient orderClient) {
		this.repository = repository;
		this.productClient = productClient;
		this.orderClient = orderClient;
	}

	public CartResponse getCart(UUID userId) {
		CartDocument cart = repository.findByUserId(userId).orElseGet(() -> emptyCart(userId));
		return toResponse(cart);
	}

	public CartResponse addItem(UUID userId, AddCartItemRequest req) {
		ProductQuoteItemResponse quoted = quoteSingle(req.productId());
		validateProductForCart(quoted, req.quantity());

		CartDocument cart = repository.findByUserId(userId).orElseGet(() -> emptyCart(userId));
		CartItem item = cart.getItems().stream().filter(i -> req.productId().equals(i.getProductId())).findFirst().orElse(null);

		if (item == null) {
			item = new CartItem();
			item.setProductId(req.productId());
			item.setAddedAt(Instant.now());
			cart.getItems().add(item);
		}

		item.setQuantity(req.quantity());
		item.setUnitPrice(quoted.priceCents().setScale(2, RoundingMode.HALF_UP));
		item.setCurrency(quoted.currency());
		item.setNameSnapshot("product-" + quoted.productId());

		refreshTotalsAndTtl(cart);
		repository.save(cart);
		return toResponse(cart);
	}

	public CartResponse updateItem(UUID userId, UUID productId, UpdateCartItemRequest req) {
		CartDocument cart = repository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Cart not found for user"));

		ProductQuoteItemResponse quoted = quoteSingle(productId);
		validateProductForCart(quoted, req.quantity());

		CartItem existing = cart.getItems().stream().filter(i -> productId.equals(i.getProductId())).findFirst()
				.orElseThrow(() -> new NotFoundException("Item not found in cart"));

		existing.setQuantity(req.quantity());
		existing.setUnitPrice(quoted.priceCents().setScale(2, RoundingMode.HALF_UP));
		existing.setCurrency(quoted.currency());
		refreshTotalsAndTtl(cart);
		repository.save(cart);
		return toResponse(cart);
	}

	public CartResponse removeItem(UUID userId, UUID productId) {
		CartDocument cart = repository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Cart not found for user"));

		boolean removed = cart.getItems().removeIf(i -> productId.equals(i.getProductId()));
		if (!removed) {
			throw new NotFoundException("Item not found in cart");
		}

		refreshTotalsAndTtl(cart);
		repository.save(cart);
		return toResponse(cart);
	}

	public void clear(UUID userId) {
		repository.deleteByUserId(userId);
	}

	public CheckoutResponse checkout(UUID userId, CheckoutRequest req) {
		CartDocument cart = repository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Cart not found for user"));
		if (cart.getItems().isEmpty()) {
			throw new BadRequestException("Cannot checkout an empty cart");
		}

		revalidateCart(cart);

		List<CreateOrderItemRequest> orderItems = cart.getItems().stream()
				.map(i -> new CreateOrderItemRequest(i.getProductId(), i.getQuantity())).toList();

		OrderResponse order = orderClient.createOrder(new CreateOrderRequest(orderItems, req.shippingAddress(), req.paymentMethod()));
		repository.deleteByUserId(userId);

		return new CheckoutResponse(order.id(), order.status(), "Order created and cart cleared");
	}

	private void revalidateCart(CartDocument cart) {
		String ids = cart.getItems().stream().map(i -> i.getProductId().toString()).collect(Collectors.joining(","));
		ProductQuoteResponse quote = productClient.quote(ids);
		Map<UUID, ProductQuoteItemResponse> quotedById = quote.items().stream()
				.collect(Collectors.toMap(ProductQuoteItemResponse::productId, Function.identity()));

		for (CartItem item : cart.getItems()) {
			ProductQuoteItemResponse quoted = quotedById.get(item.getProductId());
			if (quoted == null) {
				throw new BadRequestException("Some products in cart are no longer available");
			}
			validateProductForCart(quoted, item.getQuantity());
			item.setUnitPrice(quoted.priceCents().setScale(2, RoundingMode.HALF_UP));
			item.setCurrency(quoted.currency());
		}
		refreshTotalsAndTtl(cart);
	}

	private ProductQuoteItemResponse quoteSingle(UUID productId) {
		ProductQuoteResponse quote = productClient.quote(productId.toString());
		if (quote.items() == null || quote.items().isEmpty()) {
			throw new NotFoundException("Product not found");
		}
		return quote.items().get(0);
	}

	private void validateProductForCart(ProductQuoteItemResponse quoted, Integer quantity) {
		if (!quoted.exists()) {
			throw new NotFoundException("Product not found");
		}
		if (!quoted.active()) {
			throw new BadRequestException("Product is inactive");
		}
		if (quoted.stock() < quantity) {
			throw new BadRequestException("Insufficient stock for product " + quoted.productId());
		}
	}

	private CartDocument emptyCart(UUID userId) {
		CartDocument cart = new CartDocument();
		cart.setId("cart_" + userId);
		cart.setUserId(userId);
		cart.setItems(new ArrayList<>());
		refreshTotalsAndTtl(cart);
		return cart;
	}

	private void refreshTotalsAndTtl(CartDocument cart) {
		BigDecimal subtotal = BigDecimal.ZERO;
		String currency = null;
		for (CartItem item : cart.getItems()) {
			if (item.getUnitPrice() == null || item.getQuantity() == null) {
				continue;
			}
			subtotal = subtotal.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
			if (currency == null) {
				currency = item.getCurrency();
			}
		}
		cart.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
		cart.setCurrency(currency == null ? "USD" : currency);
		cart.setUpdatedAt(Instant.now());
		cart.setExpiresAt(Instant.now().plus(CART_TTL_DAYS, ChronoUnit.DAYS));
	}

	private CartResponse toResponse(CartDocument cart) {
		List<CartItemResponse> items = cart.getItems().stream()
				.map(i -> new CartItemResponse(i.getProductId(), i.getQuantity(), i.getUnitPrice(), i.getCurrency(),
						i.getNameSnapshot()))
				.toList();
		return new CartResponse(cart.getUserId(), items, cart.getSubtotal(), cart.getCurrency(), cart.getUpdatedAt(),
				cart.getExpiresAt());
	}
}
