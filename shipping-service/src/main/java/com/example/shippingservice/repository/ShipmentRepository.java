package com.example.shippingservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shippingservice.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

}
