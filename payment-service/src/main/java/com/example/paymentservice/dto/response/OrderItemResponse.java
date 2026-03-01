package com.example.paymentservice.dto.response;

import java.util.UUID;

public record OrderItemResponse(
	UUID productId,

	int quantity,

	long unitPriceCents,

	String currency
) {

}
