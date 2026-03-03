package com.example.paymentservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.paymentservice.config.FeignConfig;
import com.example.paymentservice.dto.request.UpdateOrderRequest;
import com.example.paymentservice.dto.response.OrderResponse;

import jakarta.validation.Valid;

@FeignClient(name = "${order-service.name:order-service}", configuration = FeignConfig.class)
public interface OrderClient {

	@GetMapping("/orders/{orderId}")
	OrderResponse getById(@PathVariable("orderId") UUID orderId);

	@PutMapping("/orders/{orderId}")
	OrderResponse update(@PathVariable("orderId") UUID orderId, @Valid @RequestBody UpdateOrderRequest req);

}
