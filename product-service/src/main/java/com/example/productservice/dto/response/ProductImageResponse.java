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

	public UUID getId() {
		return id;
	}

	public UUID getProductId() {
		return productId;
	}

	public String getUrl() {
		return url;
	}

	public String getAltText() {
		return altText;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
