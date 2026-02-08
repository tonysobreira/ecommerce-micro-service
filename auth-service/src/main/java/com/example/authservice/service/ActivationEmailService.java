package com.example.authservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ActivationEmailService {

	private final JavaMailSender mailSender;
	private final String fromAddress;
	private final String activationBaseUrl;

	public ActivationEmailService(JavaMailSender mailSender,
			@Value("${app.mail.from:no-reply@ecommerce.local}") String fromAddress,
			@Value("${app.activation.base-url:http://localhost:8081/auth/activate}") String activationBaseUrl) {
		this.mailSender = mailSender;
		this.fromAddress = fromAddress;
		this.activationBaseUrl = activationBaseUrl;
	}

	public void sendActivationEmail(String email, String token) {
		String activationLink = activationBaseUrl + "?token=" + token;
		String body = "Welcome! Please activate your account by clicking this link: " + activationLink;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(fromAddress);
			helper.setTo(email);
			helper.setSubject("Activate your account");
			helper.setText(body, false);
			mailSender.send(message);
		}
		catch (MessagingException | MailException ex) {
			throw new IllegalStateException("Failed to send activation email", ex);
		}
	}
}
