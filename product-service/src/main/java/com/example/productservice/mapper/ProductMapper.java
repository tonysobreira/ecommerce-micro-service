package com.example.productservice.mapper;

import com.example.productservice.model.Product;
import com.example.productservice.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductResponse toResponse(Product product);

}
