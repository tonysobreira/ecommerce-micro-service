package com.example.emailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActivationEmailRequest(
	@Email
	@NotBlank
	String email,

	@NotBlank
	String activationLink,

	long expiresInMinutes
) {

}
