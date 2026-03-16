package com.example.notificationservice.messaging;

import java.util.UUID;

public record ShippingEmailEvent(
	String email,
	UUID orderId,
	String eventType,
	String details
) {
}
