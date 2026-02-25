package com.example.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@Email
	@NotBlank
	String email,

	@NotBlank
	String password
) {

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
