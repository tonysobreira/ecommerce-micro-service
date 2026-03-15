package com.example.notificationservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderStatusEmailRequest(
	@Email
	@NotBlank
	String email,

	@NotNull
	UUID orderId,

	@NotBlank
	String status,

	@NotBlank
	String currency,

	@NotNull(message = "Total is required")
	@DecimalMin(value = "0.01", message = "Total must be greater than 0")
	BigDecimal totalCents
) {

}
