package com.example.productservice.service;

import com.example.productservice.model.ProductImage;
import com.example.productservice.dto.request.ProductImageCreateRequest;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {

	private final ProductImageRepository productImageRepository;
	private final ProductService productService;

	public ProductImageService(ProductImageRepository productImageRepository, ProductService productService) {
		this.productImageRepository = productImageRepository;
		this.productService = productService;
	}

	@Transactional(readOnly = true)
	public List<ProductImage> listByProduct(UUID productId) {
		return productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
	}

	@Transactional
	public ProductImage create(ProductImageCreateRequest req) {
		productService.get(req.getProductId()); // validate exists
		int sortOrder = req.getSortOrder() != null ? req.getSortOrder() : 0;
		ProductImage img = new ProductImage(UUID.randomUUID(), req.getProductId(), req.getUrl().trim(),
				req.getAltText(), sortOrder, Instant.now());
		return productImageRepository.save(img);
	}

	@Transactional
	public void delete(UUID id) {
		ProductImage img = productImageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image not found"));
		productImageRepository.delete(img);
	}

}
