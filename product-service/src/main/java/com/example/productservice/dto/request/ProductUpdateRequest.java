package com.example.productservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
	UUID categoryId,

	@Size(max = 200)
	String name,

	@Size(max = 255)
	String description,

	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	BigDecimal priceCents,

	@Size(max = 10)
	String currency,

	Boolean active
) {

}
