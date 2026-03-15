package com.example.shippingservice.controller;

import com.example.shippingservice.dto.request.CreateMethodRequest;
import com.example.shippingservice.dto.request.CreateShipmentRequest;
import com.example.shippingservice.dto.request.TrackingRequest;
import com.example.shippingservice.model.Shipment;
import com.example.shippingservice.model.ShippingMethod;
import com.example.shippingservice.model.Tracking;
import com.example.shippingservice.service.ShippingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
	private final ShippingService shippingService;

	public ShippingController(ShippingService shippingService) {
		this.shippingService = shippingService;
	}


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
