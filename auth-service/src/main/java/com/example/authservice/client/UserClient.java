package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.authservice.dto.user.CreateUserProfileRequest;

@FeignClient(name = "${user-service.name:user-service}", path = "/internal/users")
public interface UserClient {

	@PostMapping("/profiles")
	void createProfileIfMissing(@RequestBody CreateUserProfileRequest request);

}
