package com.example.authservice.service;

import com.example.authservice.client.EmailClient;
import com.example.authservice.dto.email.ActivationEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActivationEmailService {

	private static final Logger log = LoggerFactory.getLogger(ActivationEmailService.class);

	private final EmailClient emailClient;

	private final String activationBaseUrl;

	private final long activationTtlMinutes;

	public ActivationEmailService(EmailClient emailClient,
			@Value("${app.activation.base-url:http://localhost:8080/auth/activate}") String activationBaseUrl,
			@Value("${security.activation.ttl-minutes:30}") long activationTtlMinutes) {
		this.emailClient = emailClient;
		this.activationBaseUrl = activationBaseUrl;
		this.activationTtlMinutes = activationTtlMinutes;
	}

	public void sendActivationEmail(String email, String token) {
		String activationLink = activationBaseUrl + "?token=" + token;
		try {
			emailClient.sendActivation(new ActivationEmailRequest(email, activationLink, activationTtlMinutes));
			log.info("Activation email dispatch requested for {}", email);
		} catch (Exception ex) {
			log.error("Failed to dispatch activation email for {}", email, ex);
			throw new IllegalStateException("Failed to send activation email.", ex);
		}
	}

}
