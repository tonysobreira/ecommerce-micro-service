package com.example.paymentservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.paymentservice.model.PaymentMethod;
import com.example.paymentservice.model.PaymentStatus;

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