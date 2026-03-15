package com.example.cartservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CartItem {

	private UUID productId;

	private Integer quantity;

	private BigDecimal unitPrice;

	private String currency;

	private String nameSnapshot;

	private Instant addedAt;

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNameSnapshot() {
		return nameSnapshot;
	}

	public void setNameSnapshot(String nameSnapshot) {
		this.nameSnapshot = nameSnapshot;
	}

	public Instant getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(Instant addedAt) {
		this.addedAt = addedAt;
	}

}
