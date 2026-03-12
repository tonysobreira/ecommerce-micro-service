package com.example.authservice.messaging;

public record PasswordResetEmailEvent(String email, String resetLink, long expiresInMinutes) {
}

