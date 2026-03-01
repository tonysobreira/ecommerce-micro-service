package com.example.paymentservice.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentservice.client.OrderClient;
import com.example.paymentservice.client.UserClient;
import com.example.paymentservice.dto.request.CreatePaymentRequest;
import com.example.paymentservice.dto.response.OrderResponse;
import com.example.paymentservice.dto.response.PaymentResponse;
import com.example.paymentservice.dto.response.UserResponse;
import com.example.paymentservice.exception.BadRequestException;
import com.example.paymentservice.exception.NotFoundException;
import com.example.paymentservice.exception.PaymentNotFoundException;
import com.example.paymentservice.mapper.PaymentMapper;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;

@Service
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;

	private final PaymentMapper paymentMapper;

	private final UserClient userClient;

	private final OrderClient orderClient;

	public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, UserClient userClient,
			OrderClient orderClient) {
		this.paymentRepository = paymentRepository;
		this.paymentMapper = paymentMapper;
		this.userClient = userClient;
		this.orderClient = orderClient;
	}

	@Transactional
	public PaymentResponse processPayment(String email, CreatePaymentRequest request) {

		try {
			UserResponse user = userClient.findById(UUID.fromString(request.userId()));
			if (Objects.isNull(user)) {
				throw new NotFoundException("User not found.");
			}

			OrderResponse order = orderClient.getById(UUID.fromString(request.orderId()));
			if (Objects.isNull(order)) {
				throw new NotFoundException("Order not found.");
			}
		} catch (Exception ex) {
			throw new BadRequestException(ex.getMessage());
		}

		paymentRepository.findByOrderId(request.orderId()).ifPresent(p -> {
			throw new BadRequestException("Payment already processed.");
		});

		Payment payment = Payment.builder().orderId(request.orderId()).userId(request.userId()).amount(request.amount())
				.paymentMethod(request.paymentMethod()).build();

		payment = paymentRepository.save(payment);

		try {
			payment.setStatus(PaymentStatus.COMPLETED);
			payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			log.info("Payment processed successfully: {}", payment.getId());
		} catch (Exception e) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason(e.getMessage());
			log.error("Payment processing failed: {}", e.getMessage());
		}

		return paymentMapper.toResponse(paymentRepository.save(payment));
	}

	@Transactional(readOnly = true)
	public PaymentResponse getPaymentById(String id) {
		return paymentRepository.findById(id).map(paymentMapper::toResponse)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));
	}

	@Transactional(readOnly = true)
	public PaymentResponse getPaymentByOrderId(String orderId) {
		return paymentRepository.findByOrderId(orderId).map(paymentMapper::toResponse)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + orderId));
	}

	@Transactional(readOnly = true)
	public List<PaymentResponse> getPaymentsByUserId(String userId) {
		return paymentRepository.findByUserId(userId).stream().map(paymentMapper::toResponse).toList();
	}

	@Transactional
	public PaymentResponse refundPayment(String id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));

		if (payment.getStatus() != PaymentStatus.COMPLETED) {
			throw new IllegalStateException("Can only refund completed payments");
		}

		payment.setStatus(PaymentStatus.REFUNDED);
		log.info("Payment refunded: {}", id);
		return paymentMapper.toResponse(paymentRepository.save(payment));
	}

}