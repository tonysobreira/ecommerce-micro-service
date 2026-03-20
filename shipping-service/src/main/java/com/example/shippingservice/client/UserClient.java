package com.example.shippingservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shippingservice.config.FeignConfig;
import com.example.shippingservice.dto.response.UserAddressResponse;

@FeignClient(name = "${user-service.name:user-service}", path = "/internal/users", configuration = FeignConfig.class)
public interface UserClient {

	@GetMapping("/{userId}/addresses/{addressId}")
	UserAddressResponse findByUserIdAndUserProfileId(@PathVariable UUID userId, @PathVariable UUID addressId);

}
