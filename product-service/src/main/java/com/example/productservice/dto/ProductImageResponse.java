package com.example.productservice.dto;

import java.time.Instant;
import java.util.UUID;

public class ProductImageResponse {

	private UUID id;

	private UUID productId;

	private String url;

	private String altText;

	private int sortOrder;

	private Instant createdAt;

	public ProductImageResponse() {
	}

	public ProductImageResponse(UUID id, UUID productId, String url, String altText, int sortOrder, Instant createdAt) {
		this.id = id;
		this.productId = productId;
		this.url = url;
		this.altText = altText;
		this.sortOrder = sortOrder;
		this.createdAt = createdAt;
	}

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
