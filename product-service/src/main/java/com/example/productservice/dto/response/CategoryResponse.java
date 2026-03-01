package com.example.productservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
	UUID id,

	String name,

	Instant createdAt,

	Instant updatedAt
) {

}
