package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ProductImageCreateRequest(
	@NotNull
	UUID productId,

	@NotBlank
	String url,

	@Size(max = 200)
	String altText,

	Integer sortOrder
) {

}
