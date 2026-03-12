package com.example.orderservice.messaging;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailEventPublisher {

	private static final Logger log = LoggerFactory.getLogger(EmailEventPublisher.class);

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String orderStatusTopic;

	public EmailEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${app.kafka.topics.order-status-email:email.order-status.requested}") String orderStatusTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.orderStatusTopic = orderStatusTopic;
	}

	public void publishOrderStatus(String email, UUID orderId, String status, String currency, BigDecimal totalCents) {
		OrderStatusEmailEvent event = new OrderStatusEmailEvent(email, orderId, status, currency, totalCents);
		kafkaTemplate.send(orderStatusTopic, orderId.toString(), event);
		log.info("Order status email event published for order {}", orderId);
	}
}

