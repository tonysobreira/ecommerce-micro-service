package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${user-service.name:user-service}")
public interface UserClient {

	@GetMapping("/users/me")
	void me();

}
