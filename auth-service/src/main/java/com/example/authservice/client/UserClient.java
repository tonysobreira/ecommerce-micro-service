package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.authservice.dto.user.CreateUserProfileRequest;

@FeignClient(name = "${user-service.name:user-service}")
public interface UserClient {

	@PostMapping("/internal/users/profiles")
	void createProfileIfMissing(@RequestBody CreateUserProfileRequest request);

}

