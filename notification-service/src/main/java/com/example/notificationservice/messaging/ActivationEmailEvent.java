package com.example.notificationservice.messaging;

public record ActivationEmailEvent(
	String email,

	String activationLink,

	long expiresInMinutes
) {

}

