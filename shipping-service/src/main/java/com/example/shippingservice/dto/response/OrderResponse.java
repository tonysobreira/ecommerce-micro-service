package com.example.shippingservice.dto.response;

import java.util.UUID;

public record OrderResponse(
	UUID id,
	String customerEmail
) {
}
