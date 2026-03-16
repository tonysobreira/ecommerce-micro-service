package com.example.productservice.mapper;

import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductImage;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	CategoryResponse toResponse(Category category);

	ProductResponse toResponse(Product product);

	ProductImageResponse toResponse(ProductImage image);

}
