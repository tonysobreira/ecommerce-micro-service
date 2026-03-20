package com.example.shippingservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shippingservice.client.OrderClient;
import com.example.shippingservice.client.PaymentClient;
import com.example.shippingservice.client.UserClient;
import com.example.shippingservice.dto.request.CreateShipmentRequest;
import com.example.shippingservice.dto.request.TrackingRequest;
import com.example.shippingservice.dto.response.OrderResponse;
import com.example.shippingservice.dto.response.PaymentResponse;
import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.dto.response.TrackingResponse;
import com.example.shippingservice.dto.response.UserAddressResponse;
import com.example.shippingservice.exception.ShipmentNotFoundException;
import com.example.shippingservice.mapper.ShippingMapper;
import com.example.shippingservice.messaging.EmailEventPublisher;
import com.example.shippingservice.model.Shipment;
import com.example.shippingservice.model.ShipmentStatus;
import com.example.shippingservice.model.ShippingMethod;
import com.example.shippingservice.model.Tracking;
import com.example.shippingservice.repository.ShipmentRepository;
import com.example.shippingservice.repository.ShippingMethodRepository;
import com.example.shippingservice.repository.TrackingRepository;
import com.example.shippingservice.security.UserPrincipal;

@Service
public class ShippingService {

	private final ShippingMethodRepository methodRepository;

	private final ShipmentRepository shipmentRepository;

	private final TrackingRepository trackingRepository;

	private final OrderClient orderClient;

	private final EmailEventPublisher emailEventPublisher;

	private final ShippingMapper mapper;
	
	private final PaymentClient paymentClient;
	
	private final UserClient userClient;

	public ShippingService(ShippingMethodRepository methodRepository, ShipmentRepository shipmentRepository,
			TrackingRepository trackingRepository, OrderClient orderClient, EmailEventPublisher emailEventPublisher,
			ShippingMapper mapper, PaymentClient paymentClient, UserClient userClient) {
		this.methodRepository = methodRepository;
		this.shipmentRepository = shipmentRepository;
		this.trackingRepository = trackingRepository;
		this.orderClient = orderClient;
		this.emailEventPublisher = emailEventPublisher;
		this.mapper = mapper;
		this.paymentClient = paymentClient;
		this.userClient = userClient;
	}

	@Transactional
	public ShippingMethodResponse createMethod(String name, BigDecimal baseCost) {
		ShippingMethod method = new ShippingMethod(name, baseCost);
		return mapper.toResponse(methodRepository.save(method));
	}

	@Transactional(readOnly = true)
	public List<ShippingMethodResponse> listMethods() {
		return methodRepository.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional
	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		PaymentResponse payment = paymentClient.getPaymentById(request.paymentId());
		UserAddressResponse address = userClient.findByUserIdAndUserProfileId(payment.userId(), payment.orderId());
		
		String destinationAddress = ""; 
				
		Shipment shipment = new Shipment(payment.orderId(), payment.userId(), ShipmentStatus.CREATED,
				destinationAddress);
		shipment = shipmentRepository.save(shipment);

		Tracking tracking = new Tracking(shipment.getId(), ShipmentStatus.CREATED, "WAREHOUSE");
		trackingRepository.save(tracking);

		notifyShippingEvent(payment.orderId(), "SHIPMENT_CREATED", "Shipment created with id " + shipment.getId());

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

	@Transactional(readOnly = true)
	public ShipmentResponse getShipmentByOrderId(UUID orderId, UserPrincipal principal) {
		Shipment shipment = shipmentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)
				.orElseThrow(() -> new ShipmentNotFoundException("Shipment not found for order id: " + orderId));

		if (!principal.isAdmin() && !shipment.getUserId().equals(principal.getUserId())) {
			throw new AccessDeniedException("You are not allowed to view this shipment");
		}

		return mapper.toResponse(shipment);
	}

}
