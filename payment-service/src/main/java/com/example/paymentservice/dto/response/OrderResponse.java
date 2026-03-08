package com.example.paymentservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
	UUID id,

	UUID userId,

	String status,

	String paymentMethod,

	AddressResponse shippingAddress,

	String currency,

	BigDecimal subtotalCents,

	BigDecimal shippingCents,

	BigDecimal totalCents,

	Instant createdAt,

	Instant updatedAt,

	List<OrderItemResponse> items,

	List<OrderStatusHistoryResponse> statusHistory
) {

}
