package com.example.orderservice.dto;

import java.time.Instant;
import java.util.UUID;

public class OrderStatusHistoryResponse {

	private String status;

	private UUID changedBy;

	private Instant changedAt;

	public OrderStatusHistoryResponse() {
	}

	public OrderStatusHistoryResponse(String status, UUID changedBy, Instant changedAt) {
		this.status = status;
		this.changedBy = changedBy;
		this.changedAt = changedAt;
	}

	public String getStatus() {
		return status;
	}

	public UUID getChangedBy() {
		return changedBy;
	}

	public Instant getChangedAt() {
		return changedAt;
	}

}
