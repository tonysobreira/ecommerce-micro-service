package com.example.cartservice.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
	@NotNull
	UUID userAddressId,

	@NotBlank
	String paymentMethod
) {

}
