package com.example.inventoryservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductQuoteItemResponse(
	UUID productId,

	boolean exists,

	boolean active,

	BigDecimal priceCents,

	String currency
) {
}
