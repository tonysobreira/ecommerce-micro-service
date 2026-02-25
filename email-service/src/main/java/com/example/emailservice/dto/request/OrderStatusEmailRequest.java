package com.example.emailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class OrderStatusEmailRequest {

	@Email
	@NotBlank
	private String email;

	@NotNull
	private UUID orderId;

	@NotBlank
	private String status;

	@NotBlank
	private String currency;

	private long totalCents;

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
