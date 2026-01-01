package com.example.orderservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

	@Id
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Column(nullable = false)
	private String status;

	@Column(name = "changed_by", nullable = false)
	private UUID changedBy;

	@Column(name = "changed_at", nullable = false)
	private Instant changedAt;

	protected OrderStatusHistory() {
	}

	public OrderStatusHistory(UUID id, UUID orderId, OrderStatus status, UUID changedBy, Instant changedAt) {
		this.id = id;
		this.orderId = orderId;
		this.status = status.name();
		this.changedBy = changedBy;
		this.changedAt = changedAt;
	}

	public UUID getId() {
		return id;
	}

	public UUID getOrderId() {
		return orderId;
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
