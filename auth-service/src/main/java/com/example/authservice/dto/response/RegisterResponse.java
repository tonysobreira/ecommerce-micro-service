package com.example.authservice.dto.response;

public record RegisterResponse(
	String message
) {

	public String getMessage() {
		return message;
	}

}
