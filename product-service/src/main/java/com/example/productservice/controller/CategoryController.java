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

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService service;

	public CategoryController(CategoryService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> list() {
		return ResponseEntity.ok(service.list());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> get(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(@PathVariable("id") UUID id,
			@Valid @RequestBody CategoryUpdateRequest req) {
		return ResponseEntity.ok(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
