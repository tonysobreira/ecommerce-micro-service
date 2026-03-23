package com.example.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.cartservice.config.FeignConfig;
import com.example.cartservice.dto.request.CreateOrderRequest;
import com.example.cartservice.dto.response.OrderResponse;

@FeignClient(name = "${order-service.name:order-service}", path = "/orders", configuration = FeignConfig.class)
public interface OrderClient {

	@PostMapping
	OrderResponse createOrder(@RequestBody CreateOrderRequest req);

}
