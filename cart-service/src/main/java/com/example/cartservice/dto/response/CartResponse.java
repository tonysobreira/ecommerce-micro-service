package com.example.cartservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CartResponse(
	UUID userId,

	List<CartItemResponse> items,

	BigDecimal subtotal,

	String currency,

	Instant updatedAt,

	Instant expiresAt
) {

}
