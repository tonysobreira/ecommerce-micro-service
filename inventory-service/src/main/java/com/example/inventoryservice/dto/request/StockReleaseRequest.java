package com.example.inventoryservice.dto.request;

import java.util.List;
import java.util.UUID;

import com.example.inventoryservice.service.InventoryService.StockItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StockReleaseRequest(@NotNull UUID orderId, @NotEmpty List<@Valid StockItemRequest> items) {
	public List<StockItem> toStockItems() {
		return items.stream().map(i -> new StockItem(i.productId(), i.quantity())).toList();
	}
}
