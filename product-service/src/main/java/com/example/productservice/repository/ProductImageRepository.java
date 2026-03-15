package com.example.productservice.repository;

import com.example.productservice.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

	List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);

}
