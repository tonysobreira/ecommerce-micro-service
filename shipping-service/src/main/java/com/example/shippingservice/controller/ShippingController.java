package com.example.shippingservice.controller;

import com.example.shippingservice.model.*;
import com.example.shippingservice.service.ShippingService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
	private final ShippingService shippingService;

	public ShippingController(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	record CreateMethodRequest(@NotBlank String name, @NotNull BigDecimal baseCost) {}
	record CreateShipmentRequest(@NotNull Long orderId, @NotNull Long userId, @NotBlank String destinationAddress) {}
	record TrackingRequest(@NotBlank String status, String location) {}

	@PostMapping("/methods")
	public ShippingMethod createMethod(@RequestBody CreateMethodRequest request) {
		return shippingService.createMethod(request.name(), request.baseCost());
	}

	@PostMapping("/shipments")
	public Shipment createShipment(@RequestBody CreateShipmentRequest request) {
		return shippingService.createShipment(request.orderId(), request.userId(), request.destinationAddress());
	}

	@PostMapping("/shipments/{shipmentId}/tracking")
	public Tracking addTracking(@PathVariable Long shipmentId, @RequestBody TrackingRequest request) {
		return shippingService.addTracking(shipmentId, request.status(), request.location());
	}

	@GetMapping("/shipments/{shipmentId}/tracking")
	public List<Tracking> trackingTimeline(@PathVariable Long shipmentId) {
		return shippingService.trackingTimeline(shipmentId);
	}
}
