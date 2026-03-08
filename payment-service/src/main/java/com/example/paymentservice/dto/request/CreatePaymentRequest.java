package com.example.paymentservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.paymentservice.model.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
	@NotNull
	UUID orderId,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,

    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod
) {
	
}
