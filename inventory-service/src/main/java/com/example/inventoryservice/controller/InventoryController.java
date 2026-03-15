package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.StockReservation;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
	private final InventoryService inventoryService;

	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	record UpsertStockRequest(@NotNull Long productId, @NotNull @Min(0) Integer available) {}
	record ReserveRequest(@NotNull Long orderId, @NotNull Long productId, @NotNull @Min(1) Integer quantity) {}

	@PostMapping("/stock")
	public Inventory upsertStock(@RequestBody UpsertStockRequest request) {
		return inventoryService.upsertStock(request.productId(), request.available());
	}

	@PostMapping("/reserve")
	public StockReservation reserve(@RequestBody ReserveRequest request) {
		return inventoryService.reserve(request.orderId(), request.productId(), request.quantity());
	}
}
