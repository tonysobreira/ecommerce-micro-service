package com.example.orderservice.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
	@NotEmpty
	@Valid
	List<CreateOrderItemRequest> items,

	@NotNull
	UUID userAddressId,

	@NotNull
	String paymentMethod
) {

}
