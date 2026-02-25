package com.example.productservice.dto.response;

import java.util.UUID;

public record QuoteItemResponse(
	UUID productId,

	boolean exists,

	boolean active,

	long priceCents,

	String currency,

	int stock
) {

	public UUID getProductId() {
		return productId;
	}

	public boolean getExists() {
		return exists;
	}

	public boolean isExists() {
		return exists;
	}

	public boolean getActive() {
		return active;
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
