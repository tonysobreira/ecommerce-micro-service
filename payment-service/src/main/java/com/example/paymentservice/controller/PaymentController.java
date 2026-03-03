package com.example.paymentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentservice.dto.request.CreatePaymentRequest;
import com.example.paymentservice.dto.response.PaymentResponse;
import com.example.paymentservice.security.UserPrincipal;
import com.example.paymentservice.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody CreatePaymentRequest request,
			Authentication authentication) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(paymentService.processPayment(authentication.getName(), request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID id) {
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable UUID orderId) {
		return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
	}

	@GetMapping("/my-payments")
	public ResponseEntity<List<PaymentResponse>> getMyPayments(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		return ResponseEntity.ok(paymentService.getPaymentsByUserId(principal.getUserId()));
	}

	@PostMapping("/{id}/refund")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PaymentResponse> refundPayment(@PathVariable UUID id) {
		return ResponseEntity.ok(paymentService.refundPayment(id));
	}

}