package com.example.productservice.service;

import com.example.productservice.domain.Category;
import com.example.productservice.dto.CategoryCreateRequest;
import com.example.productservice.dto.CategoryUpdateRequest;
import com.example.productservice.errors.ConflictException;
import com.example.productservice.errors.NotFoundException;
import com.example.productservice.repo.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

	private final CategoryRepository repo;

	public CategoryService(CategoryRepository repo) {
		this.repo = repo;
	}

	@Transactional(readOnly = true)
	public List<Category> list() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public Category get(UUID id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
	}

	@Transactional
	public Category create(CategoryCreateRequest req) {
		repo.findByNameIgnoreCase(req.getName()).ifPresent(c -> {
			throw new ConflictException("Category name already exists");
		});

		Instant now = Instant.now();
		Category c = new Category(UUID.randomUUID(), req.getName().trim(), now, now);
		return repo.save(c);
	}

	@Transactional
	public Category update(UUID id, CategoryUpdateRequest req) {
		Category c = get(id);

		repo.findByNameIgnoreCase(req.getName()).ifPresent(other -> {
			if (!other.getId().equals(id)) {
				throw new ConflictException("Category name already exists");
			}
		});

		c.setName(req.getName().trim());
		c.touchUpdated();
		return repo.save(c);
	}

	@Transactional
	public void delete(UUID id) {
		Category c = get(id);
		repo.delete(c);
	}

}
