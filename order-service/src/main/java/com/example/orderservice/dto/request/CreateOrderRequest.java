package com.example.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
	@NotEmpty
	@Valid
	List<CreateOrderItemRequest> items,

	@NotNull
	@Valid
	AddressRequest shippingAddress,

	@NotNull
	String paymentMethod
) {

}
