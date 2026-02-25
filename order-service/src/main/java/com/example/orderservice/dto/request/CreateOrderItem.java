package com.example.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderItem(
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
