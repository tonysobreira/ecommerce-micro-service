package com.example.inventoryservice.dto.request;

import java.util.UUID;

public record StockItemRequest(
	UUID productId,

	Integer quantity
) {

}
