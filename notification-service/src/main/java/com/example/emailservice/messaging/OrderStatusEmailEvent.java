package com.example.emailservice.messaging;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderStatusEmailEvent(String email, UUID orderId, String status, String currency, BigDecimal totalCents) {
}

