package com.example.cartservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
	@NotNull
	@Valid
	AddressRequest shippingAddress,

	@NotBlank
	String paymentMethod
) {

}
