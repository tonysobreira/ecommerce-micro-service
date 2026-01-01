package com.example.productservice.dto;

import java.time.Instant;
import java.util.UUID;

public class CategoryResponse {

	private UUID id;

	private String name;

	private Instant createdAt;

	private Instant updatedAt;

	public CategoryResponse() {
	}

	public CategoryResponse(UUID id, String name, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
