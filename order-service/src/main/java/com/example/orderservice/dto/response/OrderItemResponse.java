package com.example.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
	UUID productId,

	int quantity,

	BigDecimal unitPriceCents,

	String currency
) {

}
