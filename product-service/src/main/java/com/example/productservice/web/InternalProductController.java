package com.example.productservice.web;

import com.example.productservice.domain.Product;
import com.example.productservice.dto.*;
import com.example.productservice.repo.ProductRepository;
import com.example.productservice.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

	private final ProductRepository products;
	private final StockService stockService;

	public InternalProductController(ProductRepository products, StockService stockService) {
		this.products = products;
		this.stockService = stockService;
	}

	/**
	 * Quote authoritative price/stock for a list of product IDs. Example:
	 * /internal/products/quote?ids=uuid1,uuid2
	 */
	@GetMapping("/quote")
	public QuoteResponse quote(@RequestParam("ids") String ids) {
		List<UUID> productIds = Arrays.stream(ids.split(",")).filter(s -> !s.isBlank()).map(String::trim)
				.map(UUID::fromString).toList();

		Map<UUID, Product> found = products.findAllById(productIds).stream()
				.collect(Collectors.toMap(Product::getId, p -> p));

		List<QuoteItemResponse> items = new ArrayList<>();
		for (UUID id : productIds) {
			Product p = found.get(id);
			if (p == null) {
				items.add(new QuoteItemResponse(id, false, false, 0, null, 0));
			} else {
				items.add(new QuoteItemResponse(id, true, p.isActive(), p.getPriceCents(), p.getCurrency(),
						p.getStock()));
			}
		}

		return new QuoteResponse(items);
	}

	@PostMapping("/stock/reserve")
	public StockReserveResponse reserve(@Valid @RequestBody StockReserveRequest req) {
		stockService.reserve(req.getItems());
		return new StockReserveResponse(true);
	}

	@PostMapping("/stock/release")
	public void release(@Valid @RequestBody StockReleaseRequest req) {
		stockService.release(req.getItems());
	}

}
