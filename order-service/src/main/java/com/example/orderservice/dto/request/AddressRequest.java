package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
	@NotBlank
	String line1,

	String line2,

	@NotBlank
	String city,

	String state,

	@NotBlank
	String zip,

	@NotBlank
	String country
) {

}
