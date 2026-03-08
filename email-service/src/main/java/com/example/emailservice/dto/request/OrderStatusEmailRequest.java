package com.example.emailservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

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

	BigDecimal totalCents
) {

}
