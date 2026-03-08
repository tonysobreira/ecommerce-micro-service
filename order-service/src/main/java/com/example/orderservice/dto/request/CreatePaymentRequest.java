package com.example.orderservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.orderservice.model.PaymentMethod;

public record CreatePaymentRequest(
		UUID orderId,
		UUID userId,
		BigDecimal amount,
		PaymentMethod paymentMethod) {

}
