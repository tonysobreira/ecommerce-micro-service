package com.example.inventoryservice.dto.response;

import java.util.UUID;

public record InventoryResponse(
	UUID id,

	UUID productId,

	Integer availableQuantity,

	Integer reservedQuantity
) {

}
