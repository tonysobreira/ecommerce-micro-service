package com.example.orderservice.client.email;

import com.example.orderservice.dto.email.OrderStatusEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${email-service.name:email-service}", configuration = com.example.orderservice.config.FeignConfig.class)
public interface EmailClient {

	@PostMapping("/emails/orders/status")
	void sendOrderStatus(@RequestBody OrderStatusEmailRequest request);
}
