package com.example.productservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

	@Id
	private UUID id;

	@Column(name = "category_id")
	private UUID categoryId;

	@Column(nullable = false)
	private String name;

	private String description;

	@Column(name = "price_cents", nullable = false)
	private long priceCents;

	@Column(nullable = false)
	private String currency;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private boolean active;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected Product() {
	}

	public Product(UUID id, UUID categoryId, String name, String description, long priceCents, String currency,
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

	public void setCategoryId(UUID categoryId) {
		this.categoryId = categoryId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPriceCents(long priceCents) {
		this.priceCents = priceCents;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void touchUpdated() {
		this.updatedAt = Instant.now();
	}

}
