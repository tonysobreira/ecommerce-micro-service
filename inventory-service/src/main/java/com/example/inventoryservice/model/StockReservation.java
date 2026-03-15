package com.example.inventoryservice.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stock_reservation")
public class StockReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long orderId;
	@Column(nullable = false)
	private Long productId;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false)
	private String status;
	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	public Long getId() { return id; }
	public Long getOrderId() { return orderId; }
	public void setOrderId(Long orderId) { this.orderId = orderId; }
	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }
	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public Instant getCreatedAt() { return createdAt; }
}
