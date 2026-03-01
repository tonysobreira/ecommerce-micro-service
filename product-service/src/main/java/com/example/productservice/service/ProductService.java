package com.example.productservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	private final CategoryService categoryService;

	public ProductService(ProductRepository productRepository, CategoryService categoryService) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
	}

	@Transactional(readOnly = true)
	public List<Product> listPublic() {
		return productRepository.findByActiveTrue();
	}

	@Transactional(readOnly = true)
	public Product get(UUID id) {
		return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
	}

	@Transactional
	public Product create(ProductCreateRequest req) {
		Category category = null;
		if (req.categoryId() != null) {
			category = categoryService.get(req.categoryId());
		}
		Instant now = Instant.now();
		Product p = new Product(UUID.randomUUID(), req.categoryId(), category, req.name().trim(), req.description(),
				req.priceCents(), req.currency().trim(), req.stock(), req.active() != null ? req.active() : true, now,
				now);
		return productRepository.save(p);
	}

	@Transactional
	public Product update(UUID id, ProductUpdateRequest req) {
		Product p = get(id);

		if (req.categoryId() != null) {
			categoryService.get(req.categoryId());
			p.setCategoryId(req.categoryId());
		}

		if (req.name() != null) {
			p.setName(req.name().trim());
		}

		if (req.description() != null) {
			p.setDescription(req.description());
		}

		if (req.priceCents() != null) {
			p.setPriceCents(req.priceCents());
		}

		if (req.currency() != null) {
			p.setCurrency(req.currency().trim());
		}

		if (req.stock() != null) {
			p.setStock(req.stock());
		}

		if (req.active() != null) {
			p.setActive(req.active());
		}

		p.touchUpdated();
		return productRepository.save(p);
	}

	@Transactional
	public void delete(UUID id) {
		Product p = get(id);
		productRepository.delete(p);
	}

}
