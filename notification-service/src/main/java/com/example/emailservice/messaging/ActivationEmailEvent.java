package com.example.emailservice.messaging;

public record ActivationEmailEvent(String email, String activationLink, long expiresInMinutes) {
}

