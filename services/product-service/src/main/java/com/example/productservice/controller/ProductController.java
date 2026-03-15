package com.example.productservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;


	private final ProductMapper mapper;

	public ProductController(ProductService productService, ProductMapper mapper) {
		this.productService = productService;
		this.mapper = mapper;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> list() {
		return ResponseEntity.ok(productService.listPublic().stream().map(mapper::toResponse).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> get(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(mapper.toResponse(productService.get(id)));
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(productService.create(req)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest req) {
		return ResponseEntity.ok(mapper.toResponse(productService.update(id, req)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}


}
