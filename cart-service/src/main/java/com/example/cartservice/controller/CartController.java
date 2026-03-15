package com.example.cartservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartservice.dto.request.AddCartItemRequest;
import com.example.cartservice.dto.request.CheckoutRequest;
import com.example.cartservice.dto.request.UpdateCartItemRequest;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.dto.response.CheckoutResponse;
import com.example.cartservice.security.UserPrincipal;
import com.example.cartservice.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService service;

	public CartController(CartService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<CartResponse> get(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.getCart(principal.getUserId()));
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddCartItemRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(principal.getUserId(), req));
	}

	@PatchMapping("/items/{productId}")
	public ResponseEntity<CartResponse> updateItem(@PathVariable("productId") UUID productId,
			@Valid @RequestBody UpdateCartItemRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.updateItem(principal.getUserId(), productId, req));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeItem(@PathVariable("productId") UUID productId, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.removeItem(principal.getUserId(), productId));
	}

	@DeleteMapping
	public ResponseEntity<Void> clear(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		service.clear(principal.getUserId());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/checkout")
	public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.checkout(principal.getUserId(), req));
	}

}
