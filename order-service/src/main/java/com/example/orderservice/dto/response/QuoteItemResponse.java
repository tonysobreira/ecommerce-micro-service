package com.example.orderservice.dto.response;

import java.util.UUID;

public record QuoteItemResponse(
	UUID productId,

	boolean exists,

	boolean active,

	long priceCents,

	String currency,

	int stock
) {

}
