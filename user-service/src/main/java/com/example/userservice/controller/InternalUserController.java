package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.request.CreateUserProfileRequest;
import com.example.userservice.service.UserProfileService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

	private final UserProfileService userProfileService;

	public InternalUserController(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}

	@PostMapping("/profiles")
	public ResponseEntity<Void> createProfileIfMissing(@Valid @RequestBody CreateUserProfileRequest request) {
		userProfileService.createIfMissing(request.id(), request.email());
		return ResponseEntity.noContent().build();
	}

}
