package com.example.productservice.service;

import com.example.productservice.domain.ProductImage;
import com.example.productservice.dto.ProductImageCreateRequest;
import com.example.productservice.errors.NotFoundException;
import com.example.productservice.repo.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {

	private final ProductImageRepository repo;
	private final ProductService productService;

	public ProductImageService(ProductImageRepository repo, ProductService productService) {
		this.repo = repo;
		this.productService = productService;
	}

	@Transactional(readOnly = true)
	public List<ProductImage> listByProduct(UUID productId) {
		return repo.findByProductIdOrderBySortOrderAsc(productId);
	}

	@Transactional
	public ProductImage create(ProductImageCreateRequest req) {
		productService.get(req.getProductId()); // validate exists
		int sortOrder = req.getSortOrder() != null ? req.getSortOrder() : 0;
		ProductImage img = new ProductImage(UUID.randomUUID(), req.getProductId(), req.getUrl().trim(),
				req.getAltText(), sortOrder, Instant.now());
		return repo.save(img);
	}

	@Transactional
	public void delete(UUID id) {
		ProductImage img = repo.findById(id).orElseThrow(() -> new NotFoundException("Image not found"));
		repo.delete(img);
	}

}
