package com.example.shippingservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shippingservice.config.FeignConfig;
import com.example.shippingservice.dto.response.OrderResponse;

@FeignClient(name = "${order-service.name:order-service}", path = "/internal/orders", configuration = FeignConfig.class)
public interface InternalOrderClient {

	@GetMapping("/{orderId}")
	OrderResponse getById(@PathVariable("orderId") UUID orderId);

}
