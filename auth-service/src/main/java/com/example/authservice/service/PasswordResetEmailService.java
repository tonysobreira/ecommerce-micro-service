package com.example.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authservice.client.EmailClient;
import com.example.authservice.dto.email.PasswordResetEmailRequest;

@Service
public class PasswordResetEmailService {

	private static final Logger log = LoggerFactory.getLogger(PasswordResetEmailService.class);

	private final EmailClient emailClient;

	private final String passwordResetBaseUrl;

	private final long passwordResetTtlMinutes;

	public PasswordResetEmailService(EmailClient emailClient,
			@Value("${app.password-reset.base-url:http://localhost:4200/reset-password}") String passwordResetBaseUrl,
			@Value("${security.password-reset.ttl-minutes:30}") long passwordResetTtlMinutes) {
		this.emailClient = emailClient;
		this.passwordResetBaseUrl = passwordResetBaseUrl;
		this.passwordResetTtlMinutes = passwordResetTtlMinutes;
	}

	public void sendPasswordResetEmail(String email, String token) {
		String resetLink = passwordResetBaseUrl + "?token=" + token;
		try {
			emailClient.sendPasswordReset(new PasswordResetEmailRequest(email, resetLink, passwordResetTtlMinutes));
			log.info("Password reset email dispatch requested for {}", email);
		} catch (Exception ex) {
			log.error("Failed to dispatch password reset email for {}", email, ex);
			throw new IllegalStateException("Failed to send password reset email.", ex);
		}
	}
}
