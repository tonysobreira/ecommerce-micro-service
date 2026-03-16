package com.example.shippingservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.dto.response.TrackingResponse;
import com.example.shippingservice.mapper.ShippingMapper;
import com.example.shippingservice.model.Shipment;
import com.example.shippingservice.model.ShippingMethod;
import com.example.shippingservice.model.Tracking;
import com.example.shippingservice.repository.ShipmentRepository;
import com.example.shippingservice.repository.ShippingMethodRepository;
import com.example.shippingservice.repository.TrackingRepository;

@Service
public class ShippingService {

	private final ShippingMethodRepository methodRepository;

	private final ShipmentRepository shipmentRepository;

	private final TrackingRepository trackingRepository;

	private final ShippingMapper mapper;

	public ShippingService(ShippingMethodRepository methodRepository, ShipmentRepository shipmentRepository,
			TrackingRepository trackingRepository, ShippingMapper mapper) {
		this.methodRepository = methodRepository;
		this.shipmentRepository = shipmentRepository;
		this.trackingRepository = trackingRepository;
		this.mapper = mapper;
	}

	public ShippingMethodResponse createMethod(String name, BigDecimal baseCost) {
		ShippingMethod method = new ShippingMethod(name, baseCost);
		return mapper.toResponse(methodRepository.save(method));
	}

	@Transactional
	public ShipmentResponse createShipment(UUID orderId, UUID userId, String destinationAddress) {
		Shipment shipment = new Shipment(orderId, userId, "CREATED", destinationAddress);
		shipment = shipmentRepository.save(shipment);

		Tracking tracking = new Tracking(shipment.getId(), "CREATED", "WAREHOUSE");
		trackingRepository.save(tracking);

		return mapper.toResponse(shipment);
	}

	public TrackingResponse addTracking(UUID shipmentId, String status, String location) {
		Tracking tracking = new Tracking(shipmentId, status, location);
		return mapper.toResponse(trackingRepository.save(tracking));
	}

	public List<TrackingResponse> trackingTimeline(UUID shipmentId) {
		List<Tracking> list = trackingRepository.findByShipmentIdOrderByEventAtDesc(shipmentId);
		return list.stream().map(mapper::toResponse).toList();
	}

}
