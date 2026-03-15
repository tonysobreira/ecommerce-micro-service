package com.example.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventoryservice.model.StockReservation;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {

}
