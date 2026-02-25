package com.example.productservice.controller;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.service.CategoryService;
import jakarta.validation.Valid;
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
	public List<CategoryResponse> list() {
		return service.list().stream().map(mapper::toResponse).toList();
	}

	@GetMapping("/{id}")
	public CategoryResponse get(@PathVariable("id") UUID id) {
		return mapper.toResponse(service.get(id));
	}

	@PostMapping
	public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest req) {
		return mapper.toResponse(service.create(req));
	}

	@PutMapping("/{id}")
	public CategoryResponse update(@PathVariable("id") UUID id, @Valid @RequestBody CategoryUpdateRequest req) {
		return mapper.toResponse(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") UUID id) {
		service.delete(id);
	}

}
