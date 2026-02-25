package com.example.orderservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusHistoryResponse(
	String status,

	UUID changedBy,

	Instant changedAt
) {

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
