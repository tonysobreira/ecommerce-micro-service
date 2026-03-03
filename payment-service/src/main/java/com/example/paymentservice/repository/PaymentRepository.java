package com.example.paymentservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
	Optional<Payment> findByOrderId(UUID orderId);

	List<Payment> findByUserId(UUID userId);

	List<Payment> findByStatus(PaymentStatus status);
}
