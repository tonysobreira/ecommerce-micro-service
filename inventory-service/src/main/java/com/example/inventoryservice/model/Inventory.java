package com.example.inventoryservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private Long productId;
	@Column(nullable = false)
	private Integer available;
	@Column(nullable = false)
	private Integer reserved;

	public Long getId() { return id; }
	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }
	public Integer getAvailable() { return available; }
	public void setAvailable(Integer available) { this.available = available; }
	public Integer getReserved() { return reserved; }
	public void setReserved(Integer reserved) { this.reserved = reserved; }
}
