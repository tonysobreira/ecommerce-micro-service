package com.example.productservice.dto;

import java.util.UUID;

public class QuoteItemResponse {

	private UUID productId;

	private boolean exists;

	private boolean active;

	private long priceCents;

	private String currency;

	private int stock;

	public QuoteItemResponse() {
	}

	public QuoteItemResponse(UUID productId, boolean exists, boolean active, long priceCents, String currency,
			int stock) {
		this.productId = productId;
		this.exists = exists;
		this.active = active;
		this.priceCents = priceCents;
		this.currency = currency;
		this.stock = stock;
	}

	public UUID getProductId() {
		return productId;
	}

	public boolean isExists() {
		return exists;
	}

	public boolean isActive() {
		return active;
	}

	public long getPriceCents() {
		return priceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public int getStock() {
		return stock;
	}

}
