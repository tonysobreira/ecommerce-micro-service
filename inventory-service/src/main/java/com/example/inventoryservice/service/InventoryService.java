package com.example.inventoryservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.request.StockItemRequest;
import com.example.inventoryservice.dto.response.AvailabilityItemResponse;
import com.example.inventoryservice.dto.response.InventoryQuoteItemResponse;
import com.example.inventoryservice.dto.response.InventoryQuoteResponse;
import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.dto.response.ProductQuoteItemResponse;
import com.example.inventoryservice.mapper.InventoryMapper;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.StockMovement;
import com.example.inventoryservice.model.StockReservation;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.StockMovementRepository;
import com.example.inventoryservice.repository.StockReservationRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	private final StockReservationRepository reservationRepository;

	private final StockMovementRepository movementRepository;

	private final ProductClient productClient;

	private final InventoryMapper inventoryMapper;

	public InventoryService(InventoryRepository inventoryRepository, StockReservationRepository reservationRepository,
			StockMovementRepository movementRepository, ProductClient productClient, InventoryMapper inventoryMapper) {
		this.inventoryRepository = inventoryRepository;
		this.reservationRepository = reservationRepository;
		this.movementRepository = movementRepository;
		this.productClient = productClient;
		this.inventoryMapper = inventoryMapper;
	}

	@Transactional(readOnly = true)
	public List<AvailabilityItemResponse> availability(String idsCsv) {
		List<UUID> productIds = Arrays.stream(idsCsv.split(",")).filter(s -> !s.isBlank()).map(String::trim)
				.map(UUID::fromString).toList();
		Map<UUID, Inventory> inventories = inventoryRepository.findByProductIdIn(productIds).stream()
				.collect(Collectors.toMap(Inventory::getProductId, i -> i));

		return productIds.stream().map(productId -> {
			Inventory inventory = inventories.get(productId);

			if (inventory == null) {
				return new AvailabilityItemResponse(productId, false, 0, 0);
			}

			return new AvailabilityItemResponse(productId, true, inventory.getAvailableQuantity(),
					inventory.getReservedQuantity());
		}).toList();
	}

	@Transactional(readOnly = true)
	public InventoryQuoteResponse quote(String idsCsv) {
		List<ProductQuoteItemResponse> productQuoteItems = productClient.quote(idsCsv).items();

		List<ProductQuoteItemResponse> productItems = Objects.requireNonNullElse(productQuoteItems, List.of());

		Map<UUID, AvailabilityItemResponse> availabilityByProduct = availability(idsCsv).stream()
				.collect(Collectors.toMap(AvailabilityItemResponse::productId, Function.identity(), (a, b) -> a));

		List<InventoryQuoteItemResponse> items = productItems.stream().map(p -> {
			AvailabilityItemResponse availability = availabilityByProduct.get(p.productId());
			int availableQuantity = availability == null ? 0 : availability.availableQuantity();
			int reservedQuantity = availability == null ? 0 : availability.reservedQuantity();
			return new InventoryQuoteItemResponse(p.productId(), p.exists(), p.active(), p.priceCents(), p.currency(),
					availableQuantity, reservedQuantity);
		}).toList();

		return new InventoryQuoteResponse(items);
	}

	@Transactional
	public InventoryResponse upsertStock(UUID productId, Integer availableQuantity) {
		Inventory inventory = inventoryRepository.findByProductId(productId).orElseGet(Inventory::new);

		inventory.setProductId(productId);
		inventory.setAvailableQuantity(availableQuantity);

		if (inventory.getReservedQuantity() == null) {
			inventory.setReservedQuantity(0);
		}

		return inventoryMapper.toResponse(inventoryRepository.save(inventory));
	}

	@Transactional
	public void reserve(UUID orderId, List<StockItemRequest> items) {
		for (StockItemRequest item : items) {
			Inventory inventory = inventoryRepository.findByProductId(item.productId()).orElseThrow(
					() -> new IllegalArgumentException("Inventory not found for product " + item.productId()));

			if (inventory.getAvailableQuantity() < item.quantity()) {
				throw new IllegalArgumentException("Insufficient stock for product " + item.productId());
			}

			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.quantity());
			inventory.setReservedQuantity(inventory.getReservedQuantity() + item.quantity());
			inventoryRepository.save(inventory);

			StockReservation reservation = new StockReservation(orderId, item.productId(), item.quantity(), "RESERVED");
			reservationRepository.save(reservation);

			StockMovement movement = new StockMovement(item.productId(), item.quantity(), "RESERVE",
					"Order: " + orderId);
			movementRepository.save(movement);
		}
	}

	@Transactional
	public void release(UUID orderId, List<StockItemRequest> items) {
		for (StockItemRequest item : items) {
			Inventory inventory = inventoryRepository.findByProductId(item.productId()).orElseThrow(
					() -> new IllegalArgumentException("Inventory not found for product " + item.productId()));

			int reservedToRelease = Math.min(item.quantity(), inventory.getReservedQuantity());
			inventory.setReservedQuantity(inventory.getReservedQuantity() - reservedToRelease);
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservedToRelease);
			inventoryRepository.save(inventory);

			StockReservation reservation = new StockReservation(orderId, item.productId(), reservedToRelease,
					"RELEASED");
			reservationRepository.save(reservation);

			StockMovement movement = new StockMovement(item.productId(), reservedToRelease, "RELEASE",
					"Order: " + orderId);
			movementRepository.save(movement);
		}
	}

	@Transactional
	public void commit(UUID orderId, List<StockItemRequest> items) {
		for (StockItemRequest item : items) {
			Inventory inventory = inventoryRepository.findByProductId(item.productId()).orElseThrow(
					() -> new IllegalArgumentException("Inventory not found for product " + item.productId()));

			int reservedToCommit = Math.min(item.quantity(), inventory.getReservedQuantity());
			inventory.setReservedQuantity(inventory.getReservedQuantity() - reservedToCommit);
			inventoryRepository.save(inventory);

			StockReservation reservation = new StockReservation(orderId, item.productId(), reservedToCommit,
					"COMMITTED");
			reservationRepository.save(reservation);

			StockMovement movement = new StockMovement(item.productId(), reservedToCommit, "COMMIT", "Order: " + orderId);
			movementRepository.save(movement);
		}
	}

}
