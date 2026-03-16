package com.example.shippingservice.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.shippingservice.model.ShipmentStatus;

public record TrackingResponse(
	UUID shipmentId,

	ShipmentStatus status,

	String location,

	Instant eventAt
) {

}
