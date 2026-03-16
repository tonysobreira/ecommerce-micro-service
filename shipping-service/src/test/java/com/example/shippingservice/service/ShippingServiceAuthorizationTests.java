package com.example.shippingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import com.example.shippingservice.client.OrderClient;
import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.exception.ShipmentNotFoundException;
import com.example.shippingservice.mapper.ShippingMapper;
import com.example.shippingservice.messaging.EmailEventPublisher;
import com.example.shippingservice.model.Shipment;
import com.example.shippingservice.model.ShipmentStatus;
import com.example.shippingservice.model.ShippingMethod;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.repository.ShipmentRepository;
import com.example.shippingservice.repository.ShippingMethodRepository;
import com.example.shippingservice.repository.TrackingRepository;
import com.example.shippingservice.security.UserPrincipal;

class ShippingServiceAuthorizationTests {

	private final ShippingMethodRepository methodRepository = mock(ShippingMethodRepository.class);
	private final ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
	private final TrackingRepository trackingRepository = mock(TrackingRepository.class);
	private final OrderClient orderClient = mock(OrderClient.class);
	private final EmailEventPublisher emailEventPublisher = mock(EmailEventPublisher.class);
	private final ShippingMapper mapper = mock(ShippingMapper.class);

	private final ShippingService service = new ShippingService(methodRepository, shipmentRepository, trackingRepository,
			orderClient, emailEventPublisher, mapper);

	@Test
	void getShipmentByOrderId_allowsOwner() {
		UUID orderId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Shipment shipment = shipment(orderId, userId);
		ShipmentResponse response = new ShipmentResponse(orderId, userId, ShipmentStatus.CREATED, "Address",
				Instant.now());
		UserPrincipal owner = new UserPrincipal(userId, "owner@example.com", List.of("ROLE_USER"));

		when(shipmentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)).thenReturn(Optional.of(shipment));
		when(mapper.toResponse(shipment)).thenReturn(response);

		ShipmentResponse actual = service.getShipmentByOrderId(orderId, owner);

		assertEquals(response, actual);
	}

	@Test
	void getShipmentByOrderId_deniesNonOwnerWithoutAdminRole() {
		UUID orderId = UUID.randomUUID();
		Shipment shipment = shipment(orderId, UUID.randomUUID());
		UserPrincipal otherUser = new UserPrincipal(UUID.randomUUID(), "other@example.com", List.of("ROLE_USER"));

		when(shipmentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)).thenReturn(Optional.of(shipment));

		assertThrows(AccessDeniedException.class, () -> service.getShipmentByOrderId(orderId, otherUser));
	}

	@Test
	void getShipmentByOrderId_allowsAdmin() {
		UUID orderId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		Shipment shipment = shipment(orderId, ownerId);
		ShipmentResponse response = new ShipmentResponse(orderId, ownerId, ShipmentStatus.CREATED, "Address",
				Instant.now());
		UserPrincipal admin = new UserPrincipal(UUID.randomUUID(), "admin@example.com", List.of("ROLE_ADMIN"));

		when(shipmentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)).thenReturn(Optional.of(shipment));
		when(mapper.toResponse(shipment)).thenReturn(response);

		ShipmentResponse actual = service.getShipmentByOrderId(orderId, admin);

		assertEquals(response, actual);
	}

	@Test
	void getShipmentByOrderId_throwsWhenNotFound() {
		UUID orderId = UUID.randomUUID();
		UserPrincipal user = new UserPrincipal(UUID.randomUUID(), "user@example.com", List.of("ROLE_USER"));

		when(shipmentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)).thenReturn(Optional.empty());

		assertThrows(ShipmentNotFoundException.class, () -> service.getShipmentByOrderId(orderId, user));
	}

	@Test
	void listMethods_returnsAllMethods() {
		ShippingMethod standard = new ShippingMethod("STANDARD", java.math.BigDecimal.valueOf(10));
		ShippingMethodResponse mapped = new ShippingMethodResponse("STANDARD", java.math.BigDecimal.valueOf(10));

		when(methodRepository.findAll()).thenReturn(List.of(standard));
		when(mapper.toResponse(standard)).thenReturn(mapped);

		List<ShippingMethodResponse> result = service.listMethods();

		assertEquals(List.of(mapped), result);
	}

	private Shipment shipment(UUID orderId, UUID userId) {
		Shipment shipment = new Shipment(orderId, userId, ShipmentStatus.CREATED, "Address");
		shipment.setId(UUID.randomUUID());
		return shipment;
	}

}
