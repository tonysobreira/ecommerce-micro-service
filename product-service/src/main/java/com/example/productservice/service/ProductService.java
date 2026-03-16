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
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.dto.response.QuoteItemResponse;
import com.example.productservice.dto.response.QuoteResponse;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	private final CategoryService categoryService;

	private final ProductMapper mapper;

	public ProductService(ProductRepository productRepository, CategoryService categoryService, ProductMapper mapper) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> listPublic() {
		return productRepository.findByActiveTrue().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse get(UUID id) {
		Product product = findById(id);
		return mapper.toResponse(product);
	}

	@Transactional
	public ProductResponse create(ProductCreateRequest req) {
		Category category = null;

		if (req.categoryId() != null) {
			category = categoryService.findById(req.categoryId());
		}

		Product p = new Product(UUID.randomUUID(), req.categoryId(), category, req.name().trim(), req.description(),
				req.priceCents(), req.currency().trim(), req.active() != null ? req.active() : true);
		return mapper.toResponse(productRepository.save(p));
	}

	@Transactional
	public ProductResponse update(UUID id, ProductUpdateRequest req) {
		Product p = findById(id);

		if (req.categoryId() != null) {
			categoryService.findById(req.categoryId());
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

		if (req.active() != null) {
			p.setActive(req.active());
		}

		return mapper.toResponse(productRepository.save(p));
	}

	@Transactional
	public void delete(UUID id) {
		Product p = findById(id);
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

	public Product findById(UUID id) {
		return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
	}

}
