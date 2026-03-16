package com.example.productservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.request.ProductImageCreateRequest;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.service.ProductImageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product-images")
public class ProductImageController {

	private final ProductImageService service;

	public ProductImageController(ProductImageService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<ProductImageResponse>> listByProduct(@RequestParam("productId") UUID productId) {
		return ResponseEntity.ok(service.listByProduct(productId));
	}

	@PostMapping
	public ResponseEntity<ProductImageResponse> create(@Valid @RequestBody ProductImageCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
