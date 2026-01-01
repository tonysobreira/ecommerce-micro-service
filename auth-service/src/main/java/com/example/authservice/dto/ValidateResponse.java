package com.example.authservice.dto;

import java.util.UUID;

public class ValidateResponse {

	private boolean valid;

	private UUID userId;

	private String email;

	private String[] roles;

	public ValidateResponse() {
	}

	public ValidateResponse(boolean valid, UUID userId, String email, String[] roles) {
		this.valid = valid;
		this.userId = userId;
		this.email = email;
		this.roles = roles;
	}

	public boolean isValid() {
		return valid;
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
