package com.example.paymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
	Optional<Payment> findByOrderId(String orderId);

	List<Payment> findByUserId(String userId);

	List<Payment> findByStatus(PaymentStatus status);
}
