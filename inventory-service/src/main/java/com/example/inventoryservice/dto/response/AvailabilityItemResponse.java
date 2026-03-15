package com.example.inventoryservice.dto.response;

import java.util.UUID;

public record AvailabilityItemResponse(
	UUID productId,

	boolean exists,

	int availableQuantity,

	int reservedQuantity
) {

}
