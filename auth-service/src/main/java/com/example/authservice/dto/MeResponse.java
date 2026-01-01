package com.example.authservice.dto;

import java.util.UUID;

public class MeResponse {

	private UUID userId;

	private String email;

	private String[] roles;

	public MeResponse() {
	}

	public MeResponse(UUID userId, String email, String[] roles) {
		this.userId = userId;
		this.email = email;
		this.roles = roles;
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

}
