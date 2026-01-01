package com.example.orderservice.client;

import com.example.orderservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${product-service.name:product-service}", configuration = com.example.orderservice.config.FeignConfig.class)
public interface ProductClient {

	@GetMapping("/internal/products/quote")
	QuoteResponse quote(@RequestParam("ids") String idsCsv);

	@PostMapping("/internal/products/stock/reserve")
	StockReserveResponse reserve(@RequestBody StockReserveRequest req);

	@PostMapping("/internal/products/stock/release")
	void release(@RequestBody StockReleaseRequest req);

}
