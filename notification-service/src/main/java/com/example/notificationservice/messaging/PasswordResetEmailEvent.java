package com.example.notificationservice.messaging;

public record PasswordResetEmailEvent(
	String email,

	String resetLink,

	long expiresInMinutes
) {

}

