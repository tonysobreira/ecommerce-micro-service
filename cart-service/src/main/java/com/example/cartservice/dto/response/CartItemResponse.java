package com.example.cartservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
	UUID productId,

	Integer quantity,

	BigDecimal unitPrice,

	String currency,

	String nameSnapshot
) {

}
