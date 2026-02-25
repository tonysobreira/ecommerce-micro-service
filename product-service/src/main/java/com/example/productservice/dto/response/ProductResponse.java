package com.example.productservice.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.productservice.model.Category;

public class ProductResponse {

	private UUID id;

	private UUID categoryId;

	private CategoryResponse category;

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

	public ProductResponse(UUID id, UUID categoryId, Category category, String name, String description,
			long priceCents, String currency, int stock, boolean active, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.categoryId = categoryId;
		this.category = new CategoryResponse(category.getId(), category.getName(), category.getCreatedAt(),
				category.getUpdatedAt());
		this.name = name;
		this.description = description;
		this.priceCents = priceCents;
		this.currency = currency;
		this.stock = stock;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public ProductResponse(UUID id, UUID categoryId, CategoryResponse category, String name, String description,
			long priceCents, String currency, int stock, boolean active, Instant createdAt, Instant updatedAt) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.category = category;
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

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(UUID categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryResponse getCategory() {
		return category;
	}

	public void setCategory(CategoryResponse category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPriceCents() {
		return priceCents;
	}

	public void setPriceCents(long priceCents) {
		this.priceCents = priceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

}
