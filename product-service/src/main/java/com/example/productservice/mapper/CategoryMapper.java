package com.example.productservice.mapper;

import com.example.productservice.domain.Category;
import com.example.productservice.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
	public CategoryResponse toResponse(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getCreatedAt(), category.getUpdatedAt());
	}
}
