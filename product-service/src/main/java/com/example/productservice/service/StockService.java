package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.dto.request.StockReserveItem;
import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

	private final ProductRepository products;

	public StockService(ProductRepository products) {
		this.products = products;
	}

	/**
	 * Reserve reduces stock immediately inside a DB transaction. If any product
	 * missing/inactive/insufficient stock -> rollback.
	 */
	@Transactional
	public void reserve(List<StockReserveItem> items) {
		for (StockReserveItem i : items) {
			Product p = products.findById(i.productId())
					.orElseThrow(() -> new NotFoundException("Product not found: " + i.productId()));

			if (!p.isActive()) {
				throw new BadRequestException("Product inactive: " + p.getId());
			}

			if (p.getStock() < i.quantity()) {
				throw new BadRequestException("Insufficient stock: " + p.getId());
			}

			p.setStock(p.getStock() - i.quantity());
			p.touchUpdated();
			products.save(p);
		}
	}

	/**
	 * Release increases stock back (best-effort compensation).
	 */
	@Transactional
	public void release(List<StockReserveItem> items) {
		for (StockReserveItem i : items) {
			Product p = products.findById(i.productId())
					.orElseThrow(() -> new NotFoundException("Product not found: " + i.productId()));

			p.setStock(p.getStock() + i.quantity());
			p.touchUpdated();
			products.save(p);
		}
	}

}
