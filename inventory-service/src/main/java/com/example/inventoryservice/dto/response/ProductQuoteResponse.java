package com.example.inventoryservice.dto.response;

import java.util.List;

public record ProductQuoteResponse(
	List<ProductQuoteItemResponse> items
) {
}
