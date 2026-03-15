package com.example.shippingservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shippingservice.model.Tracking;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

	List<Tracking> findByShipmentIdOrderByEventAtDesc(Long shipmentId);

}
