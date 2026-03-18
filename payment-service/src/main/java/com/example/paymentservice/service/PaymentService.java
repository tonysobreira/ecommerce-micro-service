package com.example.paymentservice.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentservice.client.OrderClient;
import com.example.paymentservice.client.UserClient;
import com.example.paymentservice.dto.request.CreatePaymentRequest;
import com.example.paymentservice.dto.request.UpdateOrderRequest;
import com.example.paymentservice.dto.response.OrderResponse;
import com.example.paymentservice.dto.response.PaymentResponse;
import com.example.paymentservice.dto.response.UserResponse;
import com.example.paymentservice.exception.BadRequestException;
import com.example.paymentservice.exception.NotFoundException;
import com.example.paymentservice.exception.PaymentNotFoundException;
import com.example.paymentservice.mapper.PaymentMapper;
import com.example.paymentservice.model.OrderStatus;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.util.MoneyUtils;

import feign.FeignException;

@Service
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;

	private final UserClient userClient;

	private final OrderClient orderClient;

	private final PaymentMapper mapper;

	private final String internalToken;

	public PaymentService(PaymentRepository paymentRepository, UserClient userClient, OrderClient orderClient,
			PaymentMapper mapper) {
		this.paymentRepository = paymentRepository;
		this.userClient = userClient;
		this.orderClient = orderClient;
		this.mapper = mapper;
		this.internalToken = internalToken;
	}

	@Transactional
	public PaymentResponse createPendingPayment(UUID authenticatedUserId, CreatePaymentRequest request) {
		UserResponse user = fetchUser(authenticatedUserId);

		if (user == null) {
			throw new NotFoundException("User not found.");
		}

		OrderResponse order = fetchOrder(request.orderId());
		validatePaymentCreationRequest(authenticatedUserId, order, request);

		paymentRepository.findByOrderId(request.orderId()).ifPresent(p -> {
			throw new BadRequestException("Payment already exists for this order.");
		});

		Payment payment = new Payment(request.orderId(), request.userId(), request.amount(), request.paymentMethod());
		payment = paymentRepository.save(payment);
		log.info("Pending payment created: {}", payment.getId());
		return mapper.toResponse(payment);
	}

	@Transactional
	public PaymentResponse processPayment(UUID authenticatedUserId, UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentId));

		if (!authenticatedUserId.equals(payment.getUserId())) {
			throw new BadRequestException("Authenticated user does not match the payment owner.");
		}

		if (payment.getStatus() != PaymentStatus.PENDING) {
			throw new BadRequestException("Only pending payments can be processed.");
		}

		OrderResponse order = fetchOrder(payment.getOrderId());
		validateProcessPaymentRequest(order, payment);

		try {
			orderClient.updateInternal(order.id(), new UpdateOrderRequest("PAID"));
			payment.setStatus(PaymentStatus.COMPLETED);
			payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT));
			log.info("Payment processed successfully: {}", payment.getId());
		} catch (FeignException ex) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason("Unable to update order status");
			log.error("Unable to update order status for order {}", order.id(), ex);
			throw new BadRequestException("Unable to complete payment due to order update failure.");
		} catch (RuntimeException ex) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason(ex.getMessage());
			log.error("Payment processing failed: {}", ex.getMessage(), ex);
			throw ex;
		} finally {
			paymentRepository.save(payment);
		}

		return mapper.toResponse(payment);
	}

	@Transactional(readOnly = true)
	public PaymentResponse getPaymentById(UUID id) {
		return paymentRepository.findById(id).map(mapper::toResponse)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));
	}

	@Transactional(readOnly = true)
	public PaymentResponse getPaymentByOrderId(UUID orderId) {
		return paymentRepository.findByOrderId(orderId).map(mapper::toResponse)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + orderId));
	}

	@Transactional(readOnly = true)
	public List<PaymentResponse> getPaymentsByUserId(UUID userId) {
		return paymentRepository.findByUserId(userId).stream().map(mapper::toResponse).toList();
	}

	@Transactional
	public PaymentResponse refundPayment(UUID id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));

		if (payment.getStatus() != PaymentStatus.COMPLETED) {
			throw new IllegalStateException("Can only refund completed payments");
		}

		OrderResponse order = fetchOrder(payment.getOrderId());
		orderClient.updateInternal(order.id(), new UpdateOrderRequest("CANCELLED"));

		payment.setStatus(PaymentStatus.REFUNDED);
		log.info("Payment refunded: {}", id);
		return mapper.toResponse(paymentRepository.save(payment));
	}

	private UserResponse fetchUser(UUID userId) {
		try {
			return userClient.findById(userId);
		} catch (FeignException.NotFound ex) {
			throw new NotFoundException("User not found.");
		} catch (FeignException.Forbidden ex) {
			throw new BadRequestException("Unable to validate user for payment.");
		}
	}

	private OrderResponse fetchOrder(UUID orderId) {
		try {
			return orderClient.getById(orderId);
		} catch (FeignException.NotFound ex) {
			throw new NotFoundException("Order not found.");
		} catch (FeignException.Forbidden ex) {
			throw new BadRequestException("You are not allowed to access this order.");
		}
	}

	private void validatePaymentCreationRequest(UUID authenticatedUserId, OrderResponse order,
			CreatePaymentRequest request) {
		if (!authenticatedUserId.equals(order.userId())) {
			throw new BadRequestException("Authenticated user does not match the order owner.");
		}

		if (!request.userId().equals(order.userId())) {
			throw new BadRequestException("Request user does not match the order owner.");
		}

		if (!OrderStatus.CREATED.name().equalsIgnoreCase(order.status())) {
			throw new BadRequestException("Order status does not allow payment: " + order.status());
		}

		if (request.amount().compareTo(MoneyUtils.centsToAmount(order.totalCents())) != 0) {
			throw new BadRequestException("Payment amount must match order total.");
		}

		if (!request.paymentMethod().name().equalsIgnoreCase(order.paymentMethod())) {
			throw new BadRequestException("Payment method must match order payment method.");
		}
	}

	private void validateProcessPaymentRequest(OrderResponse order, Payment payment) {
		if (!payment.getUserId().equals(order.userId())) {
			throw new BadRequestException("Payment owner does not match order owner.");
		}

		if (!OrderStatus.CREATED.name().equalsIgnoreCase(order.status())) {
			throw new BadRequestException("Order status does not allow payment: " + order.status());
		}

		if (payment.getAmount().compareTo(MoneyUtils.centsToAmount(order.totalCents())) != 0) {
			throw new BadRequestException("Payment amount must match order total.");
		}

		if (!payment.getPaymentMethod().name().equalsIgnoreCase(order.paymentMethod())) {
			throw new BadRequestException("Payment method must match order payment method.");
		}
	}

}
