package com.example.productservice.controller;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	private final ProductMapper mapper;

	public ProductController(ProductService service, ProductMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public List<ProductResponse> list() {
		return service.listPublic().stream().map(mapper::toResponse).toList();
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable("id") UUID id) {
		return mapper.toResponse(service.get(id));
	}

	@PostMapping
	public ProductResponse create(@Valid @RequestBody ProductCreateRequest req) {
		return mapper.toResponse(service.create(req));
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest req) {
		return mapper.toResponse(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") UUID id) {
		service.delete(id);
	}

}
