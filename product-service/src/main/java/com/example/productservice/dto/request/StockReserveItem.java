package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.UUID;

public record StockReserveItem(
	@NotNull
	UUID productId,

	@NotNull
	@Min(1)
	Integer quantity
) {

	public UUID getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

}
