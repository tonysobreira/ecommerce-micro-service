package com.example.productservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProductCreateRequest(
	UUID categoryId,

	@NotBlank
	String name,

	String description,

	@NotNull
	@Min(0)
	Long priceCents,

	@NotBlank
	String currency,

	@NotNull
	@Min(0)
	Integer stock,

	Boolean active
) {

}
