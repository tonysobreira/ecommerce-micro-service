package com.example.shippingservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shippingservice.dto.request.CreateMethodRequest;
import com.example.shippingservice.dto.request.CreateShipmentRequest;
import com.example.shippingservice.dto.request.TrackingRequest;
import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.dto.response.TrackingResponse;
import com.example.shippingservice.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

	private final ShippingService shippingService;

	public ShippingController(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	@PostMapping("/methods")
	public ResponseEntity<ShippingMethodResponse> createMethod(@RequestBody CreateMethodRequest request) {
		return ResponseEntity.ok(shippingService.createMethod(request.name(), request.baseCost()));
	}

	@PostMapping("/shipments")
	public ResponseEntity<ShipmentResponse> createShipment(@RequestBody CreateShipmentRequest request) {
		return ResponseEntity.ok(shippingService.createShipment(request));
	}

	@PostMapping("/shipments/{shipmentId}/tracking")
	public ResponseEntity<TrackingResponse> addTracking(@PathVariable UUID shipmentId,
			@RequestBody TrackingRequest request) {
		return ResponseEntity.ok(shippingService.addTracking(shipmentId, request));
	}

	@GetMapping("/shipments/{shipmentId}/tracking")
	public ResponseEntity<List<TrackingResponse>> trackingTimeline(@PathVariable UUID shipmentId) {
		return ResponseEntity.ok(shippingService.trackingTimeline(shipmentId));
	}

}
