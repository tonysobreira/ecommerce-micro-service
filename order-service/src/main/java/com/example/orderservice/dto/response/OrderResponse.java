package com.example.orderservice.dto.response;

import com.example.orderservice.dto.request.AddressDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
	UUID id,

	UUID userId,

	String status,

	String paymentMethod,

	AddressDto shippingAddress,

	String currency,

	long subtotalCents,

	long shippingCents,

	long totalCents,

	Instant createdAt,

	Instant updatedAt,

	List<OrderItemResponse> items,

	List<OrderStatusHistoryResponse> statusHistory
) {

	public UUID getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getStatus() {
		return status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public AddressDto getShippingAddress() {
		return shippingAddress;
	}

	public String getCurrency() {
		return currency;
	}

	public long getSubtotalCents() {
		return subtotalCents;
	}

	public long getShippingCents() {
		return shippingCents;
	}

	public long getTotalCents() {
		return totalCents;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public List<OrderStatusHistoryResponse> getStatusHistory() {
		return statusHistory;
	}

}
