package com.example.orderservice.dto.response;

import com.example.orderservice.dto.request.AddressDto;

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

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public AddressDto getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressDto shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public long getSubtotalCents() {
		return subtotalCents;
	}

	public void setSubtotalCents(long subtotalCents) {
		this.subtotalCents = subtotalCents;
	}

	public long getShippingCents() {
		return shippingCents;
	}

	public void setShippingCents(long shippingCents) {
		this.shippingCents = shippingCents;
	}

	public long getTotalCents() {
		return totalCents;
	}

	public void setTotalCents(long totalCents) {
		this.totalCents = totalCents;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

	public List<OrderStatusHistoryResponse> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<OrderStatusHistoryResponse> statusHistory) {
		this.statusHistory = statusHistory;
	}

}
