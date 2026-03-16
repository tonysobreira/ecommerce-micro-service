package com.example.productservice.dto.request;

import java.util.UUID;

public record UpsertStockRequest(
	UUID productId,
	Integer availableQuantity
) {

}
