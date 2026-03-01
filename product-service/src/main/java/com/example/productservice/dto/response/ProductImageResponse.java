package com.example.productservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProductImageResponse(
	UUID id,

	UUID productId,

	String url,

	String altText,

	int sortOrder,

	Instant createdAt
) {

}
