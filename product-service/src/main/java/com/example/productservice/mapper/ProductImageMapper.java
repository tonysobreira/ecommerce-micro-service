package com.example.productservice.mapper;

import com.example.productservice.model.ProductImage;
import com.example.productservice.dto.response.ProductImageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

	ProductImageResponse toResponse(ProductImage image);

}
