package com.example.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventoryservice.dto.request.StockReleaseRequest;
import com.example.inventoryservice.dto.request.StockReserveRequest;
import com.example.inventoryservice.dto.request.UpsertStockRequest;
import com.example.inventoryservice.dto.response.AvailabilityItemResponse;
import com.example.inventoryservice.dto.response.InventoryQuoteResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@PostMapping("/stock")
	public Inventory upsertStock(@RequestBody UpsertStockRequest request) {
		return inventoryService.upsertStock(request.productId(), request.availableQuantity());
	}

	@Hidden
	@GetMapping("/internal/availability")
	public ResponseEntity<List<AvailabilityItemResponse>> availability(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(inventoryService.availability(ids));
	}

	@Hidden
	@GetMapping("/internal/quote")
	public ResponseEntity<InventoryQuoteResponse> quote(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(inventoryService.quote(ids));
	}

	@Hidden
	@PostMapping("/internal/stock/reserve")
	public ResponseEntity<Void> reserve(@Valid @RequestBody StockReserveRequest request) {
		inventoryService.reserve(request.orderId(), request.items());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Hidden
	@PostMapping("/internal/stock/release")
	public ResponseEntity<Void> release(@Valid @RequestBody StockReleaseRequest request) {
		inventoryService.release(request.orderId(), request.items());
		return ResponseEntity.noContent().build();
	}

}
