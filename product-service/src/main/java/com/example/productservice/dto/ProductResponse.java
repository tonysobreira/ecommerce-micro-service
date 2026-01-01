package com.example.productservice.dto;

import java.time.Instant;
import java.util.UUID;

public class ProductResponse {

	private UUID id;

	private UUID categoryId;

	private String name;

	private String description;

	private long priceCents;

	private String currency;

	private int stock;

	private boolean active;

	private Instant createdAt;

	private Instant updatedAt;

	public ProductResponse() {
	}

	public ProductResponse(UUID id, UUID categoryId, String name, String description, long priceCents, String currency,
			int stock, boolean active, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
		this.priceCents = priceCents;
		this.currency = currency;
		this.stock = stock;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UUID getId() {
		return id;
	}

	public UUID getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getPriceCents() {
		return priceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public int getStock() {
		return stock;
	}

	public boolean isActive() {
		return active;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
