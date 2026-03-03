package com.example.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderRequest(
	@NotBlank
	String status
) {

}
