package com.example.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.example.paymentservice.config.FeignConfig;

@FeignClient(name = "${product-service.name:product-service}", configuration = FeignConfig.class)
public interface ProductClient {
	// Product service is catalog-only. Kept for future catalog queries.
}
