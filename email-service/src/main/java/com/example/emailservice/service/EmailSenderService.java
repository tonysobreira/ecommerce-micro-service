package com.example.emailservice.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.emailservice.dto.request.ActivationEmailRequest;
import com.example.emailservice.dto.request.OrderStatusEmailRequest;
import com.example.emailservice.util.MoneyUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

	private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

	private final JavaMailSender mailSender;
	private final String fromAddress;
	private final String mailUsername;

	public EmailSenderService(JavaMailSender mailSender, @Value("${app.mail.from:}") String fromAddress,
			@Value("${spring.mail.username:}") String mailUsername) {
		this.mailSender = mailSender;
		this.fromAddress = fromAddress;
		this.mailUsername = mailUsername;
	}

	public void sendActivation(ActivationEmailRequest request) {
		String body = """
				<h2>Hello %s,</h2>
				<p>Thank you for registering!</p>
				<p>Click the button below to activate your account:</p>
				<a href=\"%s\" style=\"padding:12px 24px; background:#0066cc; color:white; text-decoration:none; border-radius:6px;\">
				    Activate Account
				</a>
				<p>This link expires in %d minutes.</p>
				<p style=\"color:#666; font-size:0.9em;\">If you didn't register, please ignore this email.</p>
				"""
				.formatted(request.email(), request.activationLink(), request.expiresInMinutes());

		send(request.email(), "Activate Your Account", body);
	}

	public void sendOrderStatus(OrderStatusEmailRequest request) {
		String status = request.status().trim().toUpperCase();
		String body = """
				<h2>Your order status was updated</h2>
				<p>Order: <b>%s</b></p>
				<p>New status: <b>%s</b></p>
				<p>Total: <b>%s %.2f</b></p>
				<p>Thank you for shopping with us.</p>
				""".formatted(request.orderId(), status, request.currency(), MoneyUtils.centsToAmount(request.totalCents()));

		send(request.email(), "Order " + request.orderId() + " is " + status, body);
	}

	private void send(String to, String subject, String body) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(resolveFromAddress());
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(message);
			log.info("Email sent to {} with subject {}", to, subject);
		} catch (MessagingException | MailException ex) {
			log.error("Failed to send email to {}", to, ex);
			throw new IllegalStateException("Failed to send email", ex);
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
