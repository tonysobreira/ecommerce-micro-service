package com.example.inventoryservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryQuoteItemResponse(
	UUID productId,
	boolean exists,
	boolean active,
	BigDecimal priceCents,
	String currency,
	int availableQuantity,
	int reservedQuantity
) {
}
