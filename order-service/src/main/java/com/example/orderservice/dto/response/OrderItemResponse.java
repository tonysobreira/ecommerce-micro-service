package com.example.orderservice.dto.response;

import java.util.UUID;

public record OrderItemResponse(
	UUID productId,

	int quantity,

	long unitPriceCents,

	String currency
) {

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
