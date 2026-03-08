package com.example.userservice.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserAddressRequest(
	@Size(max = 255)
	String line1,

	@Size(max = 255)
	String line2,

	@Size(max = 120)
	String city,

	@Size(max = 120)
	String state,

	@Size(max = 40)
	String zip,

	@Size(max = 120)
	String country
) {

}
