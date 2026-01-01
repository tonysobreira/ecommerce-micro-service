package com.example.orderservice.dto;

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

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getPriceCents() {
		return priceCents;
	}

	public void setPriceCents(long priceCents) {
		this.priceCents = priceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}
