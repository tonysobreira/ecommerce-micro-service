package com.example.productservice.service;

import com.example.productservice.domain.Product;
import com.example.productservice.dto.StockReserveItem;
import com.example.productservice.errors.BadRequestException;
import com.example.productservice.errors.NotFoundException;
import com.example.productservice.repo.ProductRepository;
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
			Product p = products.findById(i.getProductId())
					.orElseThrow(() -> new NotFoundException("Product not found: " + i.getProductId()));

			if (!p.isActive()) {
				throw new BadRequestException("Product inactive: " + p.getId());
			}

			if (p.getStock() < i.getQuantity()) {
				throw new BadRequestException("Insufficient stock: " + p.getId());
			}

			p.setStock(p.getStock() - i.getQuantity());
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
			Product p = products.findById(i.getProductId())
					.orElseThrow(() -> new NotFoundException("Product not found: " + i.getProductId()));

			p.setStock(p.getStock() + i.getQuantity());
			p.touchUpdated();
			products.save(p);
		}
	}

}
