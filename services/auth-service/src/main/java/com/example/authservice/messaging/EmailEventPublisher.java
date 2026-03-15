package com.example.authservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailEventPublisher {

	private static final Logger log = LoggerFactory.getLogger(EmailEventPublisher.class);

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String activationTopic;
	private final String passwordResetTopic;

	public EmailEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${app.kafka.topics.activation-email:email.activation.requested}") String activationTopic,
			@Value("${app.kafka.topics.password-reset-email:email.password-reset.requested}") String passwordResetTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.activationTopic = activationTopic;
		this.passwordResetTopic = passwordResetTopic;
	}

	public void publishActivation(String email, String activationLink, long expiresInMinutes) {
		ActivationEmailEvent event = new ActivationEmailEvent(email, activationLink, expiresInMinutes);
		kafkaTemplate.send(activationTopic, email, event);
		log.info("Activation email event published for {}", email);
	}

	public void publishPasswordReset(String email, String resetLink, long expiresInMinutes) {
		PasswordResetEmailEvent event = new PasswordResetEmailEvent(email, resetLink, expiresInMinutes);
		kafkaTemplate.send(passwordResetTopic, email, event);
		log.info("Password reset email event published for {}", email);
	}

}

