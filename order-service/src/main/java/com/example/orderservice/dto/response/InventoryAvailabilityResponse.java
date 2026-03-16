package com.example.orderservice.dto.response;

import java.util.UUID;

public record InventoryAvailabilityResponse(
	UUID productId,

	boolean exists,

	int availableQuantity,

	int reservedQuantity
) {

}
