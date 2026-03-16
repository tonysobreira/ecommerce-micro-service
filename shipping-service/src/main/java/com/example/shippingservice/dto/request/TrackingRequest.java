package com.example.shippingservice.dto.request;

import com.example.shippingservice.model.ShipmentStatus;

import jakarta.validation.constraints.NotNull;

public record TrackingRequest(
	@NotNull
	ShipmentStatus status,

	String location
) {

}
