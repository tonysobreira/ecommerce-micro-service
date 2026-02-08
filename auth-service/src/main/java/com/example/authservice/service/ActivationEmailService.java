package com.example.authservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ActivationEmailService {
	private static final Logger log = LoggerFactory.getLogger(ActivationEmailService.class);

	private final JavaMailSender mailSender;
	private final String fromAddress;
	private final String mailUsername;
	private final String activationBaseUrl;

	public ActivationEmailService(JavaMailSender mailSender,
			@Value("${app.mail.from:}") String fromAddress, @Value("${spring.mail.username:}") String mailUsername,
			@Value("${app.activation.base-url:http://localhost:8080/auth/activate}") String activationBaseUrl) {
		this.mailSender = mailSender;
		this.fromAddress = fromAddress;
		this.mailUsername = mailUsername;
		this.activationBaseUrl = activationBaseUrl;
	}

	public void sendActivationEmail(String email, String token) {
		String activationLink = activationBaseUrl + "?token=" + token;
		String body = "Welcome! Please activate your account by clicking this link: " + activationLink;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(resolveFromAddress());
			helper.setTo(email);
			helper.setSubject("Activate your account");
			helper.setText(body, false);
			mailSender.send(message);
			log.info("Activation email sent to {}", email);
		}
		catch (MessagingException | MailException ex) {
			log.error("Failed to send activation email to {}. Activation link: {}", email, activationLink, ex);
			throw new IllegalStateException(
					"Failed to send activation email. Check SMTP config (MAIL_HOST/MAIL_PORT/MAIL_USERNAME/MAIL_PASSWORD).",
					ex);
		}
	}

	private String resolveFromAddress() {
		if (fromAddress != null && !fromAddress.isBlank()) {
			return fromAddress;
		}
		if (mailUsername != null && !mailUsername.isBlank()) {
			return mailUsername;
		}
		return "no-reply@ecommerce.local";
	}
}
