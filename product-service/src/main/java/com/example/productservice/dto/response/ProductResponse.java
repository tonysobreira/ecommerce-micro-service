package com.example.productservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
	UUID id,

	UUID categoryId,

	CategoryResponse category,

	String name,

	String description,

	long priceCents,

	String currency,

	int stock,

	boolean active,

	Instant createdAt,

	Instant updatedAt
) {

}
