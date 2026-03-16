package com.example.shippingservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shippingservice.client.OrderClient;
import com.example.shippingservice.dto.request.CreateShipmentRequest;
import com.example.shippingservice.dto.response.OrderResponse;
import com.example.shippingservice.dto.request.TrackingRequest;
import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.dto.response.TrackingResponse;
import com.example.shippingservice.mapper.ShippingMapper;
import com.example.shippingservice.messaging.EmailEventPublisher;
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

	private final OrderClient orderClient;

	private final EmailEventPublisher emailEventPublisher;

	private final ShippingMapper mapper;

	public ShippingService(ShippingMethodRepository methodRepository, ShipmentRepository shipmentRepository,
			TrackingRepository trackingRepository, OrderClient orderClient, EmailEventPublisher emailEventPublisher,
			ShippingMapper mapper) {
		this.methodRepository = methodRepository;
		this.shipmentRepository = shipmentRepository;
		this.trackingRepository = trackingRepository;
		this.orderClient = orderClient;
		this.emailEventPublisher = emailEventPublisher;
		this.mapper = mapper;
	}

	@Transactional
	public ShippingMethodResponse createMethod(String name, BigDecimal baseCost) {
		ShippingMethod method = new ShippingMethod(name, baseCost);
		return mapper.toResponse(methodRepository.save(method));
	}

	@Transactional
	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		Shipment shipment = new Shipment(request.orderId(), request.userId(), "CREATED", request.destinationAddress());
		shipment = shipmentRepository.save(shipment);

		Tracking tracking = new Tracking(shipment.getId(), "CREATED", "WAREHOUSE");
		trackingRepository.save(tracking);

		notifyShippingEvent(request.orderId(), "SHIPMENT_CREATED", "Shipment created with id " + shipment.getId());

		return mapper.toResponse(shipment);
	}

	@Transactional
	public TrackingResponse addTracking(UUID shipmentId, TrackingRequest request) {
		Tracking tracking = new Tracking(shipmentId, request.status(), request.location());
		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new IllegalArgumentException("Shipment not found: " + shipmentId));

		notifyShippingEvent(shipment.getOrderId(), "TRACKING_CREATED",
				"Tracking update: " + request.status() + " at " + request.location());

		return mapper.toResponse(trackingRepository.save(tracking));
	}

	private void notifyShippingEvent(UUID orderId, String eventType, String details) {
		OrderResponse order = orderClient.getById(orderId);
		emailEventPublisher.publishShippingUpdate(order.customerEmail(), order.id(), eventType, details);
	}

	@Transactional(readOnly = true)
	public List<TrackingResponse> trackingTimeline(UUID shipmentId) {
		List<Tracking> list = trackingRepository.findByShipmentIdOrderByEventAtDesc(shipmentId);
		return list.stream().map(mapper::toResponse).toList();
	}

}
