package com.example.inventoryservice.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StockReserveRequest(
	@NotNull
	UUID orderId,

	@NotEmpty
	List<@Valid StockItemRequest> items
) {

}
