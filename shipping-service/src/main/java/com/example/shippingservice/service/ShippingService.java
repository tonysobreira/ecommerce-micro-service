package com.example.shippingservice.service;

import com.example.shippingservice.model.*;
import com.example.shippingservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ShippingService {
	private final ShippingMethodRepository methodRepository;
	private final ShipmentRepository shipmentRepository;
	private final TrackingRepository trackingRepository;

	public ShippingService(ShippingMethodRepository methodRepository, ShipmentRepository shipmentRepository,
			TrackingRepository trackingRepository) {
		this.methodRepository = methodRepository;
		this.shipmentRepository = shipmentRepository;
		this.trackingRepository = trackingRepository;
	}

	public ShippingMethod createMethod(String name, BigDecimal baseCost) {
		ShippingMethod method = new ShippingMethod();
		method.setName(name);
		method.setBaseCost(baseCost);
		return methodRepository.save(method);
	}

	@Transactional
	public Shipment createShipment(Long orderId, Long userId, String destinationAddress) {
		Shipment shipment = new Shipment();
		shipment.setOrderId(orderId);
		shipment.setUserId(userId);
		shipment.setDestinationAddress(destinationAddress);
		shipment.setStatus("CREATED");
		shipment = shipmentRepository.save(shipment);

		Tracking tracking = new Tracking();
		tracking.setShipmentId(shipment.getId());
		tracking.setStatus("CREATED");
		tracking.setLocation("WAREHOUSE");
		trackingRepository.save(tracking);
		return shipment;
	}

	public Tracking addTracking(Long shipmentId, String status, String location) {
		Tracking tracking = new Tracking();
		tracking.setShipmentId(shipmentId);
		tracking.setStatus(status);
		tracking.setLocation(location);
		return trackingRepository.save(tracking);
	}

	public List<Tracking> trackingTimeline(Long shipmentId) {
		return trackingRepository.findByShipmentIdOrderByEventAtDesc(shipmentId);
	}
}
