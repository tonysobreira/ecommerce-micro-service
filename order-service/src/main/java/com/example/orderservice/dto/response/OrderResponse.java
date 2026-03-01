package com.example.orderservice.dto.response;

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

	long subtotalCents,

	long shippingCents,

	long totalCents,

	Instant createdAt,

	Instant updatedAt,

	List<OrderItemResponse> items,

	List<OrderStatusHistoryResponse> statusHistory
) {

}
