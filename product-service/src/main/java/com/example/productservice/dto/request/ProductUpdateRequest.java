package com.example.productservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ProductUpdateRequest(
	UUID categoryId,

	@Size(max = 200)
	String name,

	String description,

	@Min(0)
	Long priceCents,

	@Size(max = 10)
	String currency,

	@Min(0)
	Integer stock,

	Boolean active
) {

	public UUID getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getPriceCents() {
		return priceCents;
	}

	public String getCurrency() {
		return currency;
	}

	public Integer getStock() {
		return stock;
	}

	public Boolean getActive() {
		return active;
	}

}
