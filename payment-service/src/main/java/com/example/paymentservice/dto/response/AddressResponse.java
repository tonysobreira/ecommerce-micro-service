package com.example.paymentservice.dto.response;

public record AddressResponse(
	String line1,

	String line2,

	String city,

	String state,

	String zip,

	String country
) {

}
