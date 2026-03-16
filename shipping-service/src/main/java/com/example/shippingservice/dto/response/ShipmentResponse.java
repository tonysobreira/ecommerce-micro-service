package com.example.shippingservice.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.shippingservice.model.ShipmentStatus;

public record ShipmentResponse(
	UUID orderId,

	UUID userId,

	ShipmentStatus status,

	String destinationAddress,

	Instant createdAt
) {

}
