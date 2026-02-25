package com.example.productservice.mapper;

import com.example.productservice.domain.Product;
import com.example.productservice.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
	public ProductResponse toResponse(Product p) {
		return new ProductResponse(p.getId(), p.getCategoryId(), p.getCategory(), p.getName(), p.getDescription(),
				p.getPriceCents(), p.getCurrency(), p.getStock(), p.isActive(), p.getCreatedAt(), p.getUpdatedAt());
	}
}
