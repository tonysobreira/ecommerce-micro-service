package com.example.paymentservice.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentservice.dto.response.PaymentResponse;
import com.example.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/internal/payments")
public class InternalPaymentController {

	private final PaymentService paymentService;

	public InternalPaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID id) {
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}

}
