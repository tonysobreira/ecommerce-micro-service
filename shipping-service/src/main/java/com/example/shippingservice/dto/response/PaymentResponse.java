package com.example.shippingservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.shippingservice.model.PaymentMethod;
import com.example.shippingservice.model.PaymentStatus;

public record PaymentResponse(
	UUID id,

	UUID orderId,

	UUID userId,

	BigDecimal amount,

	PaymentStatus status,

	PaymentMethod paymentMethod,

	String transactionId,

	String failureReason,

	Instant createdAt,

	Instant updatedAt
) {

}