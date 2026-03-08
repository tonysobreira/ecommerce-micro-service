package com.example.productservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(
	UUID categoryId,

	@NotBlank
	String name,

	String description,

	@NotNull
	@DecimalMin("0")
	BigDecimal priceCents,

	@NotBlank
	String currency,

	@NotNull
	@Min(0)
	Integer stock,

	Boolean active
) {

}
