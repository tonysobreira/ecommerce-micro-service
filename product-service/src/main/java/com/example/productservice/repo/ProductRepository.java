package com.example.productservice.repo;

import com.example.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	List<Product> findByActiveTrue();

}
