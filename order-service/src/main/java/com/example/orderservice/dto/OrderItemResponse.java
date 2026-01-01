package com.example.orderservice.dto;

import java.util.UUID;

public class OrderItemResponse {

	private UUID productId;

	private int quantity;

	private long unitPriceCents;

	private String currency;

	public OrderItemResponse() {
	}

	public OrderItemResponse(UUID productId, int quantity, long unitPriceCents, String currency) {
		this.productId = productId;
		this.quantity = quantity;
		this.unitPriceCents = unitPriceCents;
		this.currency = currency;
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

}
