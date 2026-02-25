package com.example.orderservice.controller;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.request.OrderStatusPatchRequest;
import com.example.orderservice.security.UserPrincipal;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@PostMapping
	public OrderResponse create(@Valid @RequestBody CreateOrderRequest req, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return service.create(p.getUserId(), p.getUsername(), req);
	}

	@GetMapping("/my")
	public List<OrderResponse> my(Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return service.listMy(p.getUserId());
	}

	@GetMapping("/{orderId}")
	public OrderResponse get(@PathVariable("orderId") UUID orderId, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return service.get(p.getUserId(), p.isAdmin(), orderId);
	}

	@PatchMapping("/{orderId}/status")
	public OrderResponse updateStatus(@PathVariable("orderId") UUID orderId, @Valid @RequestBody OrderStatusPatchRequest req,
			Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		// SecurityConfig already requires ADMIN for this route, but double-check is OK:
		if (!p.isAdmin()) {
			throw new com.example.orderservice.exception.ForbiddenException("Admin only");
		}
		return service.updateStatus(p.getUserId(), orderId, req.getStatus());
	}

}
