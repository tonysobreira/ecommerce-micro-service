package com.example.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
	@NotEmpty
	@Valid
	List<CreateOrderItem> items,

	@NotNull
	@Valid
	AddressDto shippingAddress,

	@NotNull
	String paymentMethod
) {

	public List<CreateOrderItem> getItems() {
		return items;
	}

	public AddressDto getShippingAddress() {
		return shippingAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

}
