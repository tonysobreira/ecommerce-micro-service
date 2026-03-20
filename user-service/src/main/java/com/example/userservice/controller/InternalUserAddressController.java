package com.example.userservice.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.response.UserAddressResponse;
import com.example.userservice.service.UserAddressService;

@RestController
@RequestMapping("/internal/users/{userId}/addresses")
public class InternalUserAddressController {

	private final UserAddressService userAddressService;

	public InternalUserAddressController(UserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<UserAddressResponse> get(@PathVariable UUID userId, @PathVariable UUID addressId) {
		return ResponseEntity.ok(userAddressService.getById(userId, addressId));
	}

}
