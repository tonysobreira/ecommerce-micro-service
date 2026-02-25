package com.example.productservice.mapper;

import com.example.productservice.domain.ProductImage;
import com.example.productservice.dto.response.ProductImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {
	public ProductImageResponse toResponse(ProductImage image) {
		return new ProductImageResponse(image.getId(), image.getProductId(), image.getUrl(), image.getAltText(),
				image.getSortOrder(), image.getCreatedAt());
	}
}
