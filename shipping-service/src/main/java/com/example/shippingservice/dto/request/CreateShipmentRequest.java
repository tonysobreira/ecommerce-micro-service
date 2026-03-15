package com.example.shippingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateShipmentRequest(
	@NotNull Long orderId,
	@NotNull Long userId,
	@NotBlank String destinationAddress
) {
}
