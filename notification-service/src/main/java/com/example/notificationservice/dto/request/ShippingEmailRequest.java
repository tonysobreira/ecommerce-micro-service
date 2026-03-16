package com.example.notificationservice.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShippingEmailRequest(
	@NotBlank @Email String email,
	@NotNull UUID orderId,
	@NotBlank String eventType,
	@NotBlank String details
) {
}
