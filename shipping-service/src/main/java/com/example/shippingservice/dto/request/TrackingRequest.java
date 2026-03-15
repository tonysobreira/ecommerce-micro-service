package com.example.shippingservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TrackingRequest(
	@NotBlank
	String status,

	String location
) {

}
