package com.example.authservice.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivationEmailRequest (
	@Email
	@NotBlank
	String email,

	@NotBlank
	String activationLink,

	@NotNull
	long expiresInMinutes
) { 

}
