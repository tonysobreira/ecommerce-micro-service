package com.example.shippingservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shippingservice.config.FeignConfig;
import com.example.shippingservice.dto.response.PaymentResponse;

@FeignClient(name = "${payment-service.name:payment-service}", path = "/internal/payments", configuration = FeignConfig.class)
public interface PaymentClient {

	@GetMapping("/{id}")
	PaymentResponse getPaymentById(@PathVariable UUID id);

}
