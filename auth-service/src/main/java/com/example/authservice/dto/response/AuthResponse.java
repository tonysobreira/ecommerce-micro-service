package com.example.authservice.dto.response;

import java.util.UUID;

public record AuthResponse(
	UUID userId,

	String email,

	String[] roles,

	String accessToken,

	String refreshToken
) {

	public UUID getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public String[] getRoles() {
		return roles;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
