package com.example.productservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_images")
public class ProductImage {

	@Id
	private UUID id;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(nullable = false)
	private String url;

	@Column(name = "alt_text")
	private String altText;

	@Column(name = "sort_order", nullable = false)
	private int sortOrder;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected ProductImage() {
	}

	public ProductImage(UUID id, UUID productId, String url, String altText, int sortOrder, Instant createdAt) {
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
