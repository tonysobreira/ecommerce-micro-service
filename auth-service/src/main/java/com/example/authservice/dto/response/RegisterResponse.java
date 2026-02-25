package com.example.authservice.dto.response;

public class RegisterResponse {

	private final String message;

	public RegisterResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
