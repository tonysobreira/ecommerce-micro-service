package com.example.cartservice.dto.response;

import java.util.UUID;

public record InventoryAvailabilityResponse(
	UUID productId,
	boolean exists,
	int availableQuantity,
	int reservedQuantity
) {

}
