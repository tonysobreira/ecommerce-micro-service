package com.example.inventoryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventoryservice.dto.request.UpsertStockRequest;
import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.mapper.InventoryMapper;
import com.example.inventoryservice.service.InventoryService;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	public InventoryController(InventoryService inventoryService, InventoryMapper mapper) {
		this.inventoryService = inventoryService;
	}

	@PostMapping("/stock")
	public ResponseEntity<InventoryResponse> upsertStock(@RequestBody UpsertStockRequest request) {
		return ResponseEntity.ok().body(inventoryService.upsertStock(request.productId(), request.availableQuantity()));
	}

}
