package com.example.productservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.QuoteItemResponse;
import com.example.productservice.dto.response.QuoteResponse;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
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

		Product p = new Product(UUID.randomUUID(), req.categoryId(), category, req.name().trim(), req.description(),
				req.priceCents(), req.currency().trim(), req.active() != null ? req.active() : true);
		return productRepository.save(p);
	}

	@Transactional
	public Product update(UUID id, ProductUpdateRequest req) {
		Product p = get(id);

		if (req.categoryId() != null) {
			categoryService.get(req.categoryId());
			p.setCategoryId(req.categoryId());
		}
		if (req.name() != null)
			p.setName(req.name().trim());
		if (req.description() != null)
			p.setDescription(req.description());
		if (req.priceCents() != null)
			p.setPriceCents(req.priceCents());
		if (req.currency() != null)
			p.setCurrency(req.currency().trim());
		if (req.active() != null)
			p.setActive(req.active());

		return productRepository.save(p);
	}

	@Transactional
	public void delete(UUID id) {
		Product p = get(id);
		productRepository.delete(p);
	}

	public QuoteResponse quote(String ids) {
		List<UUID> productIds = Arrays.stream(ids.split(",")).filter(s -> !s.isBlank()).map(String::trim)
				.map(UUID::fromString).toList();

		Map<UUID, Product> found = productRepository.findAllById(productIds).stream()
				.collect(Collectors.toMap(Product::getId, p -> p));

		List<QuoteItemResponse> items = new ArrayList<>();
		for (UUID id : productIds) {
			Product p = found.get(id);
			if (p == null) {
				items.add(new QuoteItemResponse(id, false, false, BigDecimal.ZERO, null));
			} else {
				items.add(new QuoteItemResponse(id, true, p.isActive(), p.getPriceCents(), p.getCurrency()));
			}
		}

		return new QuoteResponse(items);
	}
}
