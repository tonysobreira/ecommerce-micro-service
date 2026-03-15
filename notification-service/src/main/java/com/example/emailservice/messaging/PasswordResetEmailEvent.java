package com.example.emailservice.messaging;

public record PasswordResetEmailEvent(String email, String resetLink, long expiresInMinutes) {
}

