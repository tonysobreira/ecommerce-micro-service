package com.example.shippingservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ShipmentResponse(
	UUID orderId,

	UUID userId,

	String status,

	String destinationAddress,

	Instant createdAt
) {

}
