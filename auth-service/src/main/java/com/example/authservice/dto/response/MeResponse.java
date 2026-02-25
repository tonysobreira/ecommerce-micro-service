package com.example.authservice.dto.response;

import java.util.UUID;

public record MeResponse(
	UUID userId,

	String email,

	String[] roles
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

}
