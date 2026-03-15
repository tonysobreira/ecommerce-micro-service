package com.example.inventoryservice.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpsertStockRequest(
	@NotNull
	UUID productId,

	@NotNull
	@Min(0)
	Integer availableQuantity
) {

}
