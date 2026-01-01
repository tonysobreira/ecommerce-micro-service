package com.example.productservice.service;

import com.example.productservice.domain.Product;
import com.example.productservice.dto.ProductCreateRequest;
import com.example.productservice.dto.ProductUpdateRequest;
import com.example.productservice.errors.NotFoundException;
import com.example.productservice.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

	private final ProductRepository repo;
	private final CategoryService categoryService;

	public ProductService(ProductRepository repo, CategoryService categoryService) {
		this.repo = repo;
		this.categoryService = categoryService;
	}

	@Transactional(readOnly = true)
	public List<Product> listPublic() {
		return repo.findByActiveTrue();
	}

	@Transactional(readOnly = true)
	public Product get(UUID id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
	}

	@Transactional
	public Product create(ProductCreateRequest req) {
		if (req.getCategoryId() != null)
			categoryService.get(req.getCategoryId()); // validate
		Instant now = Instant.now();
		Product p = new Product(UUID.randomUUID(), req.getCategoryId(), req.getName().trim(), req.getDescription(),
				req.getPriceCents(), req.getCurrency().trim(), req.getStock(),
				req.getActive() != null ? req.getActive() : true, now, now);
		return repo.save(p);
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
		return repo.save(p);
	}

	@Transactional
	public void delete(UUID id) {
		Product p = get(id);
		repo.delete(p);
	}

}
