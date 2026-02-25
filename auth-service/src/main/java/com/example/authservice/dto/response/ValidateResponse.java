package com.example.authservice.dto.response;

import java.util.UUID;

public record ValidateResponse(
	boolean valid,

	UUID userId,

	String email,

	String[] roles
) {

	public boolean getValid() {
		return valid;
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
