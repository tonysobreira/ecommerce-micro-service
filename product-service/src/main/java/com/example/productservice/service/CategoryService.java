package com.example.productservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.exception.ConflictException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Category;
import com.example.productservice.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	private final ProductMapper mapper;

	public CategoryService(CategoryRepository categoryRepository, ProductMapper mapper) {
		this.categoryRepository = categoryRepository;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<CategoryResponse> list() {
		return categoryRepository.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public CategoryResponse get(UUID id) {
		Category category = findById(id);
		return mapper.toResponse(category);
	}

	@Transactional
	public CategoryResponse create(CategoryCreateRequest req) {
		categoryRepository.findByNameIgnoreCase(req.name()).ifPresent(c -> {
			throw new ConflictException("Category name already exists");
		});

		Category c = new Category(UUID.randomUUID(), req.name().trim());
		return mapper.toResponse(categoryRepository.save(c));
	}

	@Transactional
	public CategoryResponse update(UUID id, CategoryUpdateRequest req) {
		Category c = findById(id);

		categoryRepository.findByNameIgnoreCase(req.name()).ifPresent(other -> {
			if (!other.getId().equals(id)) {
				throw new ConflictException("Category name already exists");
			}
		});

		c.setName(req.name().trim());
		return mapper.toResponse(categoryRepository.save(c));
	}

	@Transactional
	public void delete(UUID id) {
		Category c = findById(id);
		categoryRepository.delete(c);
	}

	public Category findById(UUID id) {
		return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
	}

}
