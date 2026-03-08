package com.example.authservice.dto.email;

public record PasswordResetEmailRequest(String email, String resetLink, long expiresInMinutes) {
}
