package com.example.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.response.QuoteResponse;
import com.example.productservice.service.ProductService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

	private final ProductService productService;

	public InternalProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/quote")
	public ResponseEntity<QuoteResponse> quote(@RequestParam("ids") String ids) {
		return ResponseEntity.ok(productService.quote(ids));
	}

}
