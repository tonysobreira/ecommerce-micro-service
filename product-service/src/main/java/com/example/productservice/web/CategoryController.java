package com.example.productservice.web;

import com.example.productservice.domain.Category;
import com.example.productservice.dto.CategoryCreateRequest;
import com.example.productservice.dto.CategoryResponse;
import com.example.productservice.dto.CategoryUpdateRequest;
import com.example.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService service;

	public CategoryController(CategoryService service) {
		this.service = service;
	}

	@GetMapping
	public List<CategoryResponse> list() {
		return service.list().stream().map(CategoryController::toResponse).toList();
	}

	@GetMapping("/{id}")
	public CategoryResponse get(@PathVariable UUID id) {
		return toResponse(service.get(id));
	}

	@PostMapping
	public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest req) {
		return toResponse(service.create(req));
	}

	@PutMapping("/{id}")
	public CategoryResponse update(@PathVariable UUID id, @Valid @RequestBody CategoryUpdateRequest req) {
		return toResponse(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}

	private static CategoryResponse toResponse(Category c) {
		return new CategoryResponse(c.getId(), c.getName(), c.getCreatedAt(), c.getUpdatedAt());
	}

}
