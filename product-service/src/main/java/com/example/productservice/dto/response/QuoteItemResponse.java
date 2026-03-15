package com.example.productservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record QuoteItemResponse(
	UUID productId,

	boolean exists,

	boolean active,

	BigDecimal priceCents,

	String currency
) {

}
