package com.example.authservice.dto;

import java.util.UUID;

public class AuthResponse {

	private UUID userId;

	private String email;

	private String[] roles;

	private String accessToken;

	private String refreshToken;

	public AuthResponse() {
	}

	public AuthResponse(UUID userId, String email, String[] roles, String accessToken, String refreshToken) {
		this.userId = userId;
		this.email = email;
		this.roles = roles;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

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
