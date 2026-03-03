package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderRequest(
	@NotBlank
	String status
) {

}
