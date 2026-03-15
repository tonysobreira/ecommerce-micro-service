package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.ActivationEmailRequest;
import com.example.notificationservice.dto.request.OrderStatusEmailRequest;
import com.example.notificationservice.dto.request.PasswordResetEmailRequest;
import com.example.notificationservice.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

	private final EmailSenderService emailSenderService;

	public EmailController(EmailSenderService emailSenderService) {
		this.emailSenderService = emailSenderService;
	}

	@PostMapping("/activation")
	public ResponseEntity<Void> sendActivation(@Valid @RequestBody ActivationEmailRequest request) {
		emailSenderService.sendActivation(request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/password-reset")
	public ResponseEntity<Void> sendPasswordReset(@Valid @RequestBody PasswordResetEmailRequest request) {
		emailSenderService.sendPasswordReset(request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/orders/status")
	public ResponseEntity<Void> sendOrderStatus(@Valid @RequestBody OrderStatusEmailRequest request) {
		emailSenderService.sendOrderStatus(request);
		return ResponseEntity.accepted().build();
	}

}
