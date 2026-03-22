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
import com.example.inventoryservice.dto.response.AvailabilityItemResponse;
import com.example.inventoryservice.dto.response.InventoryQuoteResponse;
import com.example.inventoryservice.mapper.InventoryMapper;
import com.example.inventoryservice.service.InventoryService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("/internal/inventory")
public class InternalInventoryController {

	private final InventoryService inventoryService;

	public InternalInventoryController(InventoryService inventoryService, InventoryMapper mapper) {
		this.inventoryService = inventoryService;
	}

	@Hidden
	@GetMapping("/availability")
	public ResponseEntity<List<AvailabilityItemResponse>> availability(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(inventoryService.availability(ids));
	}

	@Hidden
	@GetMapping("/quote")
	public ResponseEntity<InventoryQuoteResponse> quote(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(inventoryService.quote(ids));
	}

	@Hidden
	@PostMapping("/stock/reserve")
	public ResponseEntity<Void> reserve(@Valid @RequestBody StockReserveRequest request) {
		inventoryService.reserve(request.orderId(), request.items());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Hidden
	@PostMapping("/stock/release")
	public ResponseEntity<Void> release(@Valid @RequestBody StockReleaseRequest request) {
		inventoryService.release(request.orderId(), request.items());
		return ResponseEntity.noContent().build();
	}

	@Hidden
	@PostMapping("/stock/commit")
	public ResponseEntity<Void> commit(@Valid @RequestBody StockReleaseRequest request) {
		inventoryService.commit(request.orderId(), request.items());
		return ResponseEntity.noContent().build();
	}

}
