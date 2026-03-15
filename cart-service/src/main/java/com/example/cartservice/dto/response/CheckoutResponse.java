package com.example.cartservice.dto.response;

import java.util.UUID;

public record CheckoutResponse(
	UUID orderId,

	String status,

	String message
) {

}
