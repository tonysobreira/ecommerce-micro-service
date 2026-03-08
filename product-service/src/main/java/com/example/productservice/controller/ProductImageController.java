package com.example.productservice.controller;

import com.example.productservice.dto.request.ProductImageCreateRequest;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.mapper.ProductImageMapper;
import com.example.productservice.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-images")
public class ProductImageController {

	private final ProductImageService service;

	private final ProductImageMapper mapper;

	public ProductImageController(ProductImageService service, ProductImageMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public ResponseEntity<List<ProductImageResponse>> listByProduct(@RequestParam("productId") UUID productId) {
		return ResponseEntity.ok(service.listByProduct(productId).stream().map(mapper::toResponse).toList());
	}

	@PostMapping
	public ResponseEntity<ProductImageResponse> create(@Valid @RequestBody ProductImageCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(service.create(req)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
