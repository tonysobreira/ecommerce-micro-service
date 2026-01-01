package com.example.productservice.repo;

import com.example.productservice.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

	List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);

}
