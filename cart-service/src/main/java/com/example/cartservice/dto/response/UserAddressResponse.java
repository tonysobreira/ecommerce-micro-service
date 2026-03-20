package com.example.cartservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserAddressResponse(
	UUID id,

	UUID userProfileId,

	String line1,

	String line2,

	String city,

	String state,

	String zip,

	String country,

	Instant createdAt,

	Instant updatedAt
) {

}
