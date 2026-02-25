package com.example.orderservice.dto.email;

import java.util.UUID;

public class OrderStatusEmailRequest {

	private String email;
	private UUID orderId;
	private String status;
	private String currency;
	private long totalCents;

	public OrderStatusEmailRequest() {
	}

	public OrderStatusEmailRequest(String email, UUID orderId, String status, String currency, long totalCents) {
		this.email = email;
		this.orderId = orderId;
		this.status = status;
		this.currency = currency;
		this.totalCents = totalCents;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public UUID getOrderId() { return orderId; }
	public void setOrderId(UUID orderId) { this.orderId = orderId; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getCurrency() { return currency; }
	public void setCurrency(String currency) { this.currency = currency; }
	public long getTotalCents() { return totalCents; }
	public void setTotalCents(long totalCents) { this.totalCents = totalCents; }
}
