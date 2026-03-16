package com.example.productservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productservice.dto.request.ProductImageCreateRequest;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.ProductImage;
import com.example.productservice.repository.ProductImageRepository;

@Service
public class ProductImageService {

	private final ProductImageRepository productImageRepository;

	private final ProductService productService;

	private final ProductMapper mapper;

	public ProductImageService(ProductImageRepository productImageRepository, ProductService productService,
			ProductMapper mapper) {
		this.productImageRepository = productImageRepository;
		this.productService = productService;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<ProductImageResponse> listByProduct(UUID productId) {
		return productImageRepository.findByProductIdOrderBySortOrderAsc(productId).stream().map(mapper::toResponse)
				.toList();
	}

	@Transactional
	public ProductImageResponse create(ProductImageCreateRequest req) {
		// validate exists
		productService.findById(req.productId());
		int sortOrder = req.sortOrder() != null ? req.sortOrder() : 0;
		ProductImage img = new ProductImage(UUID.randomUUID(), req.productId(), req.url().trim(), req.altText(),
				sortOrder);
		return mapper.toResponse(productImageRepository.save(img));
	}

	@Transactional
	public void delete(UUID id) {
		ProductImage img = productImageRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Image not found"));
		productImageRepository.delete(img);
	}

}
