package com.example.paymentservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.paymentservice.model.PaymentMethod;
import com.example.paymentservice.model.PaymentStatus;

public record PaymentResponse(
        String id,

        String orderId,

        String userId,

        BigDecimal amount,

        PaymentStatus status,

        PaymentMethod paymentMethod,

        String transactionId,

        String failureReason,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
	
}