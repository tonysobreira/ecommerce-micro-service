package com.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
	@NotBlank
	String refreshToken
) {

	public String getRefreshToken() {
		return refreshToken;
	}

}
