package com.example.orderservice.repo;

import com.example.orderservice.domain.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {

	List<OrderStatusHistory> findByOrderIdOrderByChangedAtAsc(UUID orderId);

}
