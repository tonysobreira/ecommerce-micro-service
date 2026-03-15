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
import com.example.inventoryservice.dto.response.InventoryQuoteItemResponse;
import com.example.inventoryservice.dto.response.InventoryQuoteResponse;
import com.example.inventoryservice.dto.response.ProductQuoteItemResponse;
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

	public InventoryService(InventoryRepository inventoryRepository, StockReservationRepository reservationRepository,
			StockMovementRepository movementRepository, ProductClient productClient) {
		this.inventoryRepository = inventoryRepository;
		this.reservationRepository = reservationRepository;
		this.movementRepository = movementRepository;
		this.productClient = productClient;
	}

	@Transactional(readOnly = true)
	public List<AvailabilityItem> availability(String idsCsv) {
		List<UUID> productIds = Arrays.stream(idsCsv.split(",")).filter(s -> !s.isBlank()).map(String::trim)
				.map(UUID::fromString).toList();
		Map<UUID, Inventory> inventories = inventoryRepository.findByProductIdIn(productIds).stream()
				.collect(Collectors.toMap(Inventory::getProductId, i -> i));

		return productIds.stream().map(productId -> {
			Inventory inventory = inventories.get(productId);
			if (inventory == null) {
				return new AvailabilityItem(productId, false, 0, 0);
			}
			return new AvailabilityItem(productId, true, inventory.getAvailableQuantity(), inventory.getReservedQuantity());
		}).toList();
	}


	@Transactional(readOnly = true)
	public InventoryQuoteResponse quote(String idsCsv) {
		List<ProductQuoteItemResponse> productItems = Objects.requireNonNullElse(productClient.quote(idsCsv).items(),
				List.of());

		Map<UUID, AvailabilityItem> availabilityByProduct = availability(idsCsv).stream()
				.collect(Collectors.toMap(AvailabilityItem::productId, Function.identity(), (a, b) -> a));

		List<InventoryQuoteItemResponse> items = productItems.stream().map(p -> {
			AvailabilityItem availability = availabilityByProduct.get(p.productId());
			int availableQuantity = availability == null ? 0 : availability.availableQuantity();
			int reservedQuantity = availability == null ? 0 : availability.reservedQuantity();
			return new InventoryQuoteItemResponse(p.productId(), p.exists(), p.active(), p.priceCents(), p.currency(),
					availableQuantity, reservedQuantity);
		}).toList();

		return new InventoryQuoteResponse(items);
	}

	@Transactional
	public Inventory upsertStock(UUID productId, Integer availableQuantity) {
		Inventory inventory = inventoryRepository.findByProductId(productId).orElseGet(Inventory::new);
		inventory.setProductId(productId);
		inventory.setAvailableQuantity(availableQuantity);
		if (inventory.getReservedQuantity() == null) inventory.setReservedQuantity(0);
		return inventoryRepository.save(inventory);
	}

	@Transactional
	public void reserve(UUID orderId, List<StockItem> items) {
		for (StockItem item : items) {
			Inventory inventory = inventoryRepository.findByProductId(item.productId())
					.orElseThrow(() -> new IllegalArgumentException("Inventory not found for product " + item.productId()));
			if (inventory.getAvailableQuantity() < item.quantity()) {
				throw new IllegalArgumentException("Insufficient stock for product " + item.productId());
			}
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.quantity());
			inventory.setReservedQuantity(inventory.getReservedQuantity() + item.quantity());
			inventoryRepository.save(inventory);

			StockReservation reservation = new StockReservation();
			reservation.setOrderId(orderId);
			reservation.setProductId(item.productId());
			reservation.setQuantity(item.quantity());
			reservation.setStatus("RESERVED");
			reservationRepository.save(reservation);

			StockMovement movement = new StockMovement();
			movement.setProductId(item.productId());
			movement.setQuantity(item.quantity());
			movement.setType("RESERVE");
			movement.setReason("order:" + orderId);
			movementRepository.save(movement);
		}
	}

	@Transactional
	public void release(UUID orderId, List<StockItem> items) {
		for (StockItem item : items) {
			Inventory inventory = inventoryRepository.findByProductId(item.productId())
					.orElseThrow(() -> new IllegalArgumentException("Inventory not found for product " + item.productId()));

			int reservedToRelease = Math.min(item.quantity(), inventory.getReservedQuantity());
			inventory.setReservedQuantity(inventory.getReservedQuantity() - reservedToRelease);
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservedToRelease);
			inventoryRepository.save(inventory);

			StockReservation reservation = new StockReservation();
			reservation.setOrderId(orderId);
			reservation.setProductId(item.productId());
			reservation.setQuantity(reservedToRelease);
			reservation.setStatus("RELEASED");
			reservationRepository.save(reservation);

			StockMovement movement = new StockMovement();
			movement.setProductId(item.productId());
			movement.setQuantity(reservedToRelease);
			movement.setType("RELEASE");
			movement.setReason("order:" + orderId);
			movementRepository.save(movement);
		}
	}

	public record AvailabilityItem(UUID productId, boolean exists, int availableQuantity, int reservedQuantity) {}
	public record StockItem(UUID productId, Integer quantity) {}
}
