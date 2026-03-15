package com.example.shippingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shippingservice.model.ShippingMethod;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
}
