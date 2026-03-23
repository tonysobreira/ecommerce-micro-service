package com.example.orderservice.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.dto.request.UpdateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.service.OrderService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("/internal/orders")
public class InternalOrderController {

	private final OrderService service;

	public InternalOrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> findByOrderIdInternal(@PathVariable("orderId") UUID orderId) {
		return ResponseEntity.ok(service.findByOrderIdInternal(orderId));
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<OrderResponse> updateInternal(@PathVariable("orderId") UUID orderId,
			@Valid @RequestBody UpdateOrderRequest req) {
		return ResponseEntity.ok(service.update(orderId, req));
	}

}
