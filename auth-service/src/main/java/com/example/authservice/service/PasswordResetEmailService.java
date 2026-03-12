package com.example.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authservice.messaging.EmailEventPublisher;

@Service
public class PasswordResetEmailService {

	private static final Logger log = LoggerFactory.getLogger(PasswordResetEmailService.class);

	private final EmailEventPublisher emailEventPublisher;

	private final String passwordResetBaseUrl;

	private final long passwordResetTtlMinutes;

	public PasswordResetEmailService(EmailEventPublisher emailEventPublisher,
			@Value("${app.password-reset.base-url:http://localhost:4200/reset-password}") String passwordResetBaseUrl,
			@Value("${security.password-reset.ttl-minutes:30}") long passwordResetTtlMinutes) {
		this.emailEventPublisher = emailEventPublisher;
		this.passwordResetBaseUrl = passwordResetBaseUrl;
		this.passwordResetTtlMinutes = passwordResetTtlMinutes;
	}

	public void sendPasswordResetEmail(String email, String token) {
		String resetLink = passwordResetBaseUrl + "?token=" + token;
		try {
			emailEventPublisher.publishPasswordReset(email, resetLink, passwordResetTtlMinutes);
			log.info("Password reset email dispatch requested for {}", email);
		} catch (Exception ex) {
			log.error("Failed to dispatch password reset email for {}", email, ex);
			throw new IllegalStateException("Failed to send password reset email.", ex);
		}
	}

}
