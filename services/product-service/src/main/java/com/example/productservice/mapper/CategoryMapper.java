package com.example.productservice.mapper;

import com.example.productservice.model.Category;
import com.example.productservice.dto.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryResponse toResponse(Category category);

}
