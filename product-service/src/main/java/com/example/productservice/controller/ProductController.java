package com.example.productservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.request.StockReleaseRequest;
import com.example.productservice.dto.request.StockReserveRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.dto.response.QuoteResponse;
import com.example.productservice.dto.response.StockReserveResponse;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.StockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	private final StockService stockService;

	private final ProductMapper mapper;

	public ProductController(ProductService productService, StockService stockService, ProductMapper mapper) {
		this.productService = productService;
		this.stockService = stockService;
		this.mapper = mapper;
	}

	@GetMapping
	public List<ProductResponse> list() {
		return productService.listPublic().stream().map(mapper::toResponse).toList();
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable("id") UUID id) {
		return mapper.toResponse(productService.get(id));
	}

	@PostMapping
	public ProductResponse create(@Valid @RequestBody ProductCreateRequest req) {
		return mapper.toResponse(productService.create(req));
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest req) {
		return mapper.toResponse(productService.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") UUID id) {
		productService.delete(id);
	}

	/**
	 * Quote authoritative price/stock for a list of product IDs. Example:
	 * /products/quote?ids=uuid1,uuid2
	 */
	@GetMapping("/quote")
	public QuoteResponse quote(@RequestParam("ids") String ids) {
		QuoteResponse response = productService.quote(ids);
		return response;
	}

	@PostMapping("/stock/reserve")
	public StockReserveResponse reserve(@Valid @RequestBody StockReserveRequest req) {
		stockService.reserve(req.items());
		return new StockReserveResponse(true);
	}

	@PostMapping("/stock/release")
	public void release(@Valid @RequestBody StockReleaseRequest req) {
		stockService.release(req.items());
	}

}
