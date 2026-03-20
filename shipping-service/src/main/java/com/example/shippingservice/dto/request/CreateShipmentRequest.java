package com.example.shippingservice.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateShipmentRequest(
	@NotNull
	UUID paymentId
) {
}
