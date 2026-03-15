package com.example.authservice.messaging;

public record ActivationEmailEvent(String email, String activationLink, long expiresInMinutes) {
}

