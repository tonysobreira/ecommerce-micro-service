package com.example.orderservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.request.UpdateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.exception.ForbiddenException;
import com.example.orderservice.security.UserPrincipal;
import com.example.orderservice.service.OrderService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(p.getUserId(), p.getUsername(), req));
	}

	@GetMapping("/my")
	public ResponseEntity<List<OrderResponse>> my(Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.listMy(p.getUserId()));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> get(@PathVariable("orderId") UUID orderId, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.get(p.getUserId(), p.isAdmin(), orderId));
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<OrderResponse> update(@PathVariable("orderId") UUID orderId,
			@Valid @RequestBody UpdateOrderRequest req, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		if (!p.isAdmin()) {
			throw new ForbiddenException("Admin role required to update orders");
		}
		return ResponseEntity.ok(service.update(p.getUserId(), orderId, req));
	}

	@Hidden
	@PutMapping("/internal/{orderId}")
	public ResponseEntity<OrderResponse> updateInternal(@PathVariable("orderId") UUID orderId,
			@Valid @RequestBody UpdateOrderRequest req, @RequestHeader("X-Internal-Token") String internalToken) {
		return ResponseEntity.ok(service.updateInternal(internalToken, orderId, req));
	}

}
