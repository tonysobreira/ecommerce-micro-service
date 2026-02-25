package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(
	@NotBlank
	@Size(max = 120)
	String name
) {

	public String getName() {
		return name;
	}

}
