package com.example.inventoryservice.dto.response;

import java.util.List;

public record InventoryQuoteResponse(
	List<InventoryQuoteItemResponse> items
) {
}
