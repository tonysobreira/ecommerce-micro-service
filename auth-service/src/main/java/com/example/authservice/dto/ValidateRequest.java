package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidateRequest {

	@NotBlank
	private String token;

	public ValidateRequest() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
