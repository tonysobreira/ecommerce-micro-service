package com.example.shippingservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shippingservice.model.Tracking;

public interface TrackingRepository extends JpaRepository<Tracking, UUID> {

	List<Tracking> findByShipmentIdOrderByEventAtDesc(UUID shipmentId);

}
