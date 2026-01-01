package com.example.orderservice.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderResponse {

	private UUID id;

	private UUID userId;

	private String status;

	private String paymentMethod;

	private AddressDto shippingAddress;

	private String currency;

	private long subtotalCents;

	private long shippingCents;

	private long totalCents;

	private Instant createdAt;

	private Instant updatedAt;

	private List<OrderItemResponse> items;

	private List<OrderStatusHistoryResponse> statusHistory;

	public OrderResponse() {
	}

	public OrderResponse(UUID id, UUID userId, String status, String paymentMethod, AddressDto shippingAddress,
			String currency, long subtotalCents, long shippingCents, long totalCents, Instant createdAt,
			Instant updatedAt, List<OrderItemResponse> items, List<OrderStatusHistoryResponse> statusHistory) {
		this.id = id;
		this.userId = userId;
		this.status = status;
		this.paymentMethod = paymentMethod;
		this.shippingAddress = shippingAddress;
		this.currency = currency;
		this.subtotalCents = subtotalCents;
		this.shippingCents = shippingCents;
		this.totalCents = totalCents;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.items = items;
		this.statusHistory = statusHistory;
	}

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
