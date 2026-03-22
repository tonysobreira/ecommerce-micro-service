package com.example.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.paymentservice.config.FeignConfig;
import com.example.paymentservice.dto.request.StockReleaseRequest;

@FeignClient(name = "${inventory-service.name:inventory-service}", configuration = FeignConfig.class)
public interface InventoryClient {

	@PostMapping("/internal/inventory/stock/release")
	void release(@RequestBody StockReleaseRequest req);

}
