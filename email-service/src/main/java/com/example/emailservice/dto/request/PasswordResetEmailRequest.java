package com.example.emailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetEmailRequest(@NotBlank @Email String email, @NotBlank String resetLink,
		@Min(1) long expiresInMinutes) {
}
