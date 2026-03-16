package com.example.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.productservice.config.FeignConfig;
import com.example.productservice.dto.request.UpsertStockRequest;

@FeignClient(name = "${inventory-service.name:inventory-service}", configuration = FeignConfig.class)
public interface InventoryClient {

	@PostMapping("/inventory/stock")
	ResponseEntity<Void> upsertStock(@RequestBody UpsertStockRequest request);

}
