package com.example.emailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

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

	long totalCents
) {

	public String getEmail() {
		return email;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String getStatus() {
		return status;
	}

	public String getCurrency() {
		return currency;
	}

	public long getTotalCents() {
		return totalCents;
	}

}
