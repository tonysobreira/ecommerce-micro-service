package com.example.orderservice.dto.request;

import com.example.orderservice.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderRequest(
	@NotNull
	OrderStatus status
) {

}
