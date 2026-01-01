package com.example.orderservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(nullable = false)
	private int quantity;

	@Column(name = "unit_price_cents", nullable = false)
	private long unitPriceCents;

	@Column(nullable = false)
	private String currency;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected OrderItem() {
	}

	public OrderItem(UUID id, UUID orderId, UUID productId, int quantity, long unitPriceCents, String currency,
			Instant createdAt) {
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.unitPriceCents = unitPriceCents;
		this.currency = currency;
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public UUID getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public long getUnitPriceCents() {
		return unitPriceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
