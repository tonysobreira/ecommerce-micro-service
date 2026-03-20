package com.example.shippingservice.dto.response;

import java.util.UUID;

public record OrderResponse(
	UUID id,

	String customerEmail,

	String shipLine1,

	String shipLine2,

	String shipCity,

	String shipState,

	String shipZip,

	String shipCountry
) {
}
