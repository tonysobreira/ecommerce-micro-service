package com.example.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventoryservice.model.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
