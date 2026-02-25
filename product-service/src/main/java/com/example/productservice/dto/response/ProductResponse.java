package com.example.productservice.dto.response;

import java.time.Instant;
import java.util.UUID;
import com.example.productservice.model.Category;

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

	public UUID getId() {
		return id;
	}

	public UUID getCategoryId() {
		return categoryId;
	}

	public CategoryResponse getCategory() {
		return category;
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

	public boolean getActive() {
		return active;
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
