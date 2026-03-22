package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.orderservice.config.FeignConfig;
import com.example.orderservice.dto.request.StockReleaseRequest;
import com.example.orderservice.dto.request.StockReserveRequest;
import com.example.orderservice.dto.response.QuoteResponse;

@FeignClient(name = "${inventory-service.name:inventory-service}", path = "/internal", configuration = FeignConfig.class)
public interface InventoryClient {

	@GetMapping("/inventory/quote")
	QuoteResponse quote(@RequestParam("ids") String idsCsv);

	@PostMapping("/inventory/stock/reserve")
	void reserve(@RequestBody StockReserveRequest req);

	@PostMapping("/inventory/stock/release")
	void release(@RequestBody StockReleaseRequest req);

	@PostMapping("/inventory/stock/commit")
	void commit(@RequestBody StockReleaseRequest req);

}
