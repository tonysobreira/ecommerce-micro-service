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
		if (req.getCategoryId() != null) {
			category = categoryService.get(req.getCategoryId());
		}
		Instant now = Instant.now();
		Product p = new Product(UUID.randomUUID(), req.getCategoryId(), category, req.getName().trim(), req.getDescription(),
				req.getPriceCents(), req.getCurrency().trim(), req.getStock(),
				req.getActive() != null ? req.getActive() : true, now, now);
		return productRepository.save(p);
	}

	@Transactional
	public Product update(UUID id, ProductUpdateRequest req) {
		Product p = get(id);

		if (req.getCategoryId() != null) {
			categoryService.get(req.getCategoryId());
			p.setCategoryId(req.getCategoryId());
		}

		if (req.getName() != null) {
			p.setName(req.getName().trim());
		}

		if (req.getDescription() != null) {
			p.setDescription(req.getDescription());
		}

		if (req.getPriceCents() != null) {
			p.setPriceCents(req.getPriceCents());
		}

		if (req.getCurrency() != null) {
			p.setCurrency(req.getCurrency().trim());
		}

		if (req.getStock() != null) {
			p.setStock(req.getStock());
		}

		if (req.getActive() != null) {
			p.setActive(req.getActive());
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
