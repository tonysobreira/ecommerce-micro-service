package com.example.productservice.web;

import com.example.productservice.domain.ProductImage;
import com.example.productservice.dto.ProductImageCreateRequest;
import com.example.productservice.dto.ProductImageResponse;
import com.example.productservice.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-images")
public class ProductImageController {

	private final ProductImageService service;

	public ProductImageController(ProductImageService service) {
		this.service = service;
	}

	@GetMapping
	public List<ProductImageResponse> listByProduct(@RequestParam UUID productId) {
		return service.listByProduct(productId).stream().map(ProductImageController::toResponse).toList();
	}

	@PostMapping
	public ProductImageResponse create(@Valid @RequestBody ProductImageCreateRequest req) {
		return toResponse(service.create(req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}

	private static ProductImageResponse toResponse(ProductImage img) {
		return new ProductImageResponse(img.getId(), img.getProductId(), img.getUrl(), img.getAltText(),
				img.getSortOrder(), img.getCreatedAt());
	}

}
