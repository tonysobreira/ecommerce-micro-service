package com.example.productservice.controller;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService service;

	private final CategoryMapper mapper;

	public CategoryController(CategoryService service, CategoryMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> list() {
		return ResponseEntity.ok(service.list().stream().map(mapper::toResponse).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> get(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(mapper.toResponse(service.get(id)));
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(service.create(req)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(@PathVariable("id") UUID id,
			@Valid @RequestBody CategoryUpdateRequest req) {
		return ResponseEntity.ok(mapper.toResponse(service.update(id, req)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
