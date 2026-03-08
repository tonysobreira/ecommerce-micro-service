package com.example.productservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.exception.ConflictException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.model.Category;
import com.example.productservice.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public List<Category> list() {
		return categoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Category get(UUID id) {
		return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
	}

	@Transactional
	public Category create(CategoryCreateRequest req) {
		categoryRepository.findByNameIgnoreCase(req.name()).ifPresent(c -> {
			throw new ConflictException("Category name already exists");
		});

		Category c = new Category(UUID.randomUUID(), req.name().trim());
		return categoryRepository.save(c);
	}

	@Transactional
	public Category update(UUID id, CategoryUpdateRequest req) {
		Category c = get(id);

		categoryRepository.findByNameIgnoreCase(req.name()).ifPresent(other -> {
			if (!other.getId().equals(id)) {
				throw new ConflictException("Category name already exists");
			}
		});

		c.setName(req.name().trim());
		return categoryRepository.save(c);
	}

	@Transactional
	public void delete(UUID id) {
		Category c = get(id);
		categoryRepository.delete(c);
	}

}
