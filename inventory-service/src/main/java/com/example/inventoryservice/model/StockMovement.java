package com.example.inventoryservice.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stock_movement")
public class StockMovement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long productId;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false)
	private String type;
	private String reason;
	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	public Long getId() { return id; }
	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }
	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getReason() { return reason; }
	public void setReason(String reason) { this.reason = reason; }
	public Instant getCreatedAt() { return createdAt; }
}
