package com.example.emailservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.emailservice.dto.request.ActivationEmailRequest;
import com.example.emailservice.dto.request.OrderStatusEmailRequest;
import com.example.emailservice.dto.request.PasswordResetEmailRequest;
import com.example.emailservice.service.EmailSenderService;

@Component
public class EmailEventsConsumer {

	private static final Logger log = LoggerFactory.getLogger(EmailEventsConsumer.class);

	private final EmailSenderService emailSenderService;

	public EmailEventsConsumer(EmailSenderService emailSenderService) {
		this.emailSenderService = emailSenderService;
	}

	@KafkaListener(topics = "${app.kafka.topics.activation-email:email.activation.requested}", groupId = "${spring.kafka.consumer.group-id:email-service}")
	public void consumeActivation(ActivationEmailEvent event) {
		emailSenderService
				.sendActivation(new ActivationEmailRequest(event.email(), event.activationLink(), event.expiresInMinutes()));
		log.info("Activation email consumed for {}", event.email());
	}

	@KafkaListener(topics = "${app.kafka.topics.password-reset-email:email.password-reset.requested}", groupId = "${spring.kafka.consumer.group-id:email-service}")
	public void consumePasswordReset(PasswordResetEmailEvent event) {
		emailSenderService
				.sendPasswordReset(new PasswordResetEmailRequest(event.email(), event.resetLink(), event.expiresInMinutes()));
		log.info("Password reset email consumed for {}", event.email());
	}

	@KafkaListener(topics = "${app.kafka.topics.order-status-email:email.order-status.requested}", groupId = "${spring.kafka.consumer.group-id:email-service}")
	public void consumeOrderStatus(OrderStatusEmailEvent event) {
		emailSenderService.sendOrderStatus(
				new OrderStatusEmailRequest(event.email(), event.orderId(), event.status(), event.currency(), event.totalCents()));
		log.info("Order status email consumed for order {}", event.orderId());
	}

}

