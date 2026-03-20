package com.example.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
	UUID id,

	UUID userId,

	String customerEmail,

	String shipLine1,

	String shipLine2,

	String shipCity,

	String shipState,

	String shipZip,

	String shipCountry,

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
