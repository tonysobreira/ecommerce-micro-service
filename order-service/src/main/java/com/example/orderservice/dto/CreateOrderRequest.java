package com.example.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

	@NotEmpty
	@Valid
	private List<CreateOrderItem> items;

	@NotNull
	@Valid
	private AddressDto shippingAddress;

	@NotNull
	private String paymentMethod;

	public CreateOrderRequest() {
	}

	public List<CreateOrderItem> getItems() {
		return items;
	}

	public void setItems(List<CreateOrderItem> items) {
		this.items = items;
	}

	public AddressDto getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressDto shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
