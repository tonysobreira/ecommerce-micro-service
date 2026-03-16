package com.example.shippingservice.messaging;

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
	private final String shippingEmailTopic;

	public EmailEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${app.kafka.topics.shipping-email:email.shipping.requested}") String shippingEmailTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.shippingEmailTopic = shippingEmailTopic;
	}

	public void publishShippingUpdate(String email, UUID orderId, String eventType, String details) {
		ShippingEmailEvent event = new ShippingEmailEvent(email, orderId, eventType, details);
		kafkaTemplate.send(shippingEmailTopic, orderId.toString(), event);
		log.info("Shipping email event published for order {}", orderId);
	}

}
