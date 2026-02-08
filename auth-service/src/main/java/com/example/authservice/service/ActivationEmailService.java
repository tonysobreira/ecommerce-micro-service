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
	private final long activationTtlMinutes;

	public ActivationEmailService(JavaMailSender mailSender, @Value("${app.mail.from:}") String fromAddress,
			@Value("${spring.mail.username:}") String mailUsername,
			@Value("${app.activation.base-url:http://localhost:8080/auth/activate}") String activationBaseUrl,
			@Value("${security.activation.ttl-minutes:30}") long activationTtlMinutes) {
		this.mailSender = mailSender;
		this.fromAddress = fromAddress;
		this.mailUsername = mailUsername;
		this.activationBaseUrl = activationBaseUrl;
		this.activationTtlMinutes = activationTtlMinutes;
	}

	public void sendActivationEmail(String email, String token) {
		String activationLink = activationBaseUrl + "?token=" + token;
//		String body = "Welcome! Please activate your account by clicking this link: " + activationLink;

		String body = """
				<h2>Hello %s,</h2>
				<p>Thank you for registering!</p>
				<p>Click the button below to activate your account:</p>
				<a href="%s" style="padding:12px 24px; background:#0066cc; color:white; text-decoration:none; border-radius:6px;">
				    Activate Account
				</a>
				<p>This link expires in %d minutes.</p>
				<p style="color:#666; font-size:0.9em;">If you didn't register, please ignore this email.</p>
				"""
				.formatted(email, activationLink, this.activationTtlMinutes);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(resolveFromAddress());
			helper.setTo(email);
			helper.setSubject("Activate Your Account");
			helper.setText(body, true);
			mailSender.send(message);
			log.info("Activation email sent to {}", email);
		} catch (MessagingException | MailException ex) {
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
