package com.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ValidateRequest(
	@NotBlank
	String token
) {

	public String getToken() {
		return token;
	}

}
