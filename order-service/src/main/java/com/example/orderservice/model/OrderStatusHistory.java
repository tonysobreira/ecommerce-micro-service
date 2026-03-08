package com.example.orderservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Column(nullable = false)
	private String status;

	@Column(name = "changed_by", nullable = false)
	private UUID changedBy;

	@UpdateTimestamp
	@Column(name = "changed_at", nullable = false)
	private Instant changedAt;

	protected OrderStatusHistory() {
	}

	public OrderStatusHistory(UUID orderId, OrderStatus status, UUID changedBy) {
		this.orderId = orderId;
		this.status = status.name();
		this.changedBy = changedBy;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UUID getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(UUID changedBy) {
		this.changedBy = changedBy;
	}

	public Instant getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(Instant changedAt) {
		this.changedAt = changedAt;
	}

}
