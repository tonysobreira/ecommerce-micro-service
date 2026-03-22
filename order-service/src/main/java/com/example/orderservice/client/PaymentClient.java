package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.orderservice.config.FeignConfig;
import com.example.orderservice.dto.request.CreatePaymentRequest;

@FeignClient(name = "${payment-service.name:payment-service}", path = "/payments", configuration = FeignConfig.class)
public interface PaymentClient {

	@PostMapping
	void createPendingPayment(@RequestBody CreatePaymentRequest request);

}
