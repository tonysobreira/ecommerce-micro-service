package com.example.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.request.StockReleaseRequest;
import com.example.productservice.dto.request.StockReserveRequest;
import com.example.productservice.dto.response.QuoteResponse;
import com.example.productservice.dto.response.StockReserveResponse;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.StockService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

	private final ProductService productService;

	private final StockService stockService;

	public InternalProductController(ProductService productService, StockService stockService) {
		this.productService = productService;
		this.stockService = stockService;
	}

	@GetMapping("/quote")
	public ResponseEntity<QuoteResponse> quote(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(productService.quote(ids));
	}

	@PostMapping("/stock/reserve")
	public ResponseEntity<StockReserveResponse> reserve(@Valid @RequestBody StockReserveRequest req) {
		stockService.reserve(req.items());
		return ResponseEntity.ok(new StockReserveResponse(true));
	}

	@PostMapping("/stock/release")
	public ResponseEntity<Void> release(@Valid @RequestBody StockReleaseRequest req) {
		stockService.release(req.items());
		return ResponseEntity.noContent().build();
	}

}
