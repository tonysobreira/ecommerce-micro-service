package com.example.productservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
	UUID categoryId,

	@Size(max = 200)
	String name,

	String description,

	@Min(0)
	BigDecimal priceCents,

	@Size(max = 10)
	String currency,

	@Min(0)
	Integer stock,

	Boolean active
) {

}
