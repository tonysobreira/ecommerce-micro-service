package com.example.shippingservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TrackingResponse(
	UUID shipmentId,

	String status,

	String location,

	Instant eventAt
) {

}
