package com.example.productservice.web;

import com.example.productservice.domain.Product;
import com.example.productservice.dto.ProductCreateRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductUpdateRequest;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public List<ProductResponse> list() {
		return service.listPublic().stream().map(ProductController::toResponse).toList();
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable UUID id) {
		return toResponse(service.get(id));
	}

	@PostMapping
	public ProductResponse create(@Valid @RequestBody ProductCreateRequest req) {
		return toResponse(service.create(req));
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest req) {
		return toResponse(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}

	private static ProductResponse toResponse(Product p) {
		return new ProductResponse(p.getId(), p.getCategoryId(), p.getName(), p.getDescription(), p.getPriceCents(),
				p.getCurrency(), p.getStock(), p.isActive(), p.getCreatedAt(), p.getUpdatedAt());
	}

}
