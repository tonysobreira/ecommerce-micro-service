package com.example.orderservice.dto.request;

import com.example.orderservice.model.OrderStatus;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderRequest(
	@NotBlank
	OrderStatus status
) {

}
