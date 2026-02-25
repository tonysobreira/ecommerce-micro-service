package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
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

	public String getLine1() {
		return line1;
	}

	public String getLine2() {
		return line2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

}
