package com.example.productservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public class StockReserveItem {

	@NotNull
	private UUID productId;

	@NotNull
	@Min(1)
	private Integer quantity;

	public StockReserveItem() {
	}

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

}
