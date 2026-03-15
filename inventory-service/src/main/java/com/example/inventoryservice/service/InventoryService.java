package com.example.inventoryservice.service;

import com.example.inventoryservice.model.*;
import com.example.inventoryservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
	private final InventoryRepository inventoryRepository;
	private final StockReservationRepository reservationRepository;
	private final StockMovementRepository movementRepository;

	public InventoryService(InventoryRepository inventoryRepository, StockReservationRepository reservationRepository,
			StockMovementRepository movementRepository) {
		this.inventoryRepository = inventoryRepository;
		this.reservationRepository = reservationRepository;
		this.movementRepository = movementRepository;
	}

	@Transactional
	public Inventory upsertStock(Long productId, Integer available) {
		Inventory inventory = inventoryRepository.findByProductId(productId).orElseGet(Inventory::new);
		inventory.setProductId(productId);
		inventory.setAvailable(available);
		if (inventory.getReserved() == null) inventory.setReserved(0);
		return inventoryRepository.save(inventory);
	}

	@Transactional
	public StockReservation reserve(Long orderId, Long productId, Integer quantity) {
		Inventory inventory = inventoryRepository.findByProductId(productId)
			.orElseThrow(() -> new IllegalArgumentException("Inventory not found for product " + productId));
		if (inventory.getAvailable() < quantity) {
			throw new IllegalArgumentException("Insufficient stock for product " + productId);
		}
		inventory.setAvailable(inventory.getAvailable() - quantity);
		inventory.setReserved(inventory.getReserved() + quantity);
		inventoryRepository.save(inventory);

		StockReservation reservation = new StockReservation();
		reservation.setOrderId(orderId);
		reservation.setProductId(productId);
		reservation.setQuantity(quantity);
		reservation.setStatus("RESERVED");
		reservation = reservationRepository.save(reservation);

		StockMovement movement = new StockMovement();
		movement.setProductId(productId);
		movement.setQuantity(quantity);
		movement.setType("RESERVE");
		movement.setReason("order:" + orderId);
		movementRepository.save(movement);
		return reservation;
	}
}
