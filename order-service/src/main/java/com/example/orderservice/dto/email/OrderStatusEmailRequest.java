package com.example.orderservice.dto.email;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderStatusEmailRequest (
	@NotBlank
	@Email
	String email,
	
	@NotNull
	UUID orderId,
	
	@NotBlank
	String status,
	
	@NotBlank
	String currency,
	
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.01", message = "Amount must be greater than 0")
	long totalCents
) {

}
