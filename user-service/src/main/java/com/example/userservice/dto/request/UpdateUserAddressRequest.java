package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserAddressRequest(
	@NotBlank
	@Size(max = 255)
	String line1,

	@Size(max = 255)
	String line2,

	@NotBlank
	@Size(max = 120)
	String city,

	@NotBlank
	@Size(max = 120)
	String state,

	@NotBlank
	@Size(max = 40)
	String zip,

	@NotBlank
	@Size(max = 120)
	String country
) {

}
