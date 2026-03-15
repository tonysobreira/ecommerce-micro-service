package com.example.inventoryservice.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_reservation")
public class StockReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private UUID orderId;
	@Column(nullable = false)
	private UUID productId;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false)
	private String status;
	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	public Long getId() { return id; }
	public UUID getOrderId() { return orderId; }
	public void setOrderId(UUID orderId) { this.orderId = orderId; }
	public UUID getProductId() { return productId; }
	public void setProductId(UUID productId) { this.productId = productId; }
	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public Instant getCreatedAt() { return createdAt; }
}
