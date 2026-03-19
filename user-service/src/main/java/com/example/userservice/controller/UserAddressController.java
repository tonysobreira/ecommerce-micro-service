package com.example.userservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.request.CreateUserAddressRequest;
import com.example.userservice.dto.request.UpdateUserAddressRequest;
import com.example.userservice.dto.response.UserAddressResponse;
import com.example.userservice.exception.ForbiddenException;
import com.example.userservice.security.UserPrincipal;
import com.example.userservice.service.UserAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/addresses")
public class UserAddressController {

	private final UserAddressService userAddressService;

	public UserAddressController(UserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	@GetMapping
	public ResponseEntity<List<UserAddressResponse>> list(@PathVariable UUID userId, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return ResponseEntity.ok(userAddressService.listByUserId(userId));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<UserAddressResponse> get(@PathVariable UUID userId, @PathVariable UUID addressId,
			Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return ResponseEntity.ok(userAddressService.getById(userId, addressId));
	}

	@PostMapping
	public ResponseEntity<UserAddressResponse> create(@PathVariable UUID userId,
			@Valid @RequestBody CreateUserAddressRequest request, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return ResponseEntity.status(HttpStatus.CREATED).body(userAddressService.create(userId, request));
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<UserAddressResponse> update(@PathVariable UUID userId, @PathVariable UUID addressId,
			@Valid @RequestBody UpdateUserAddressRequest request, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return ResponseEntity.ok(userAddressService.update(userId, addressId, request));
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID addressId, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		userAddressService.delete(userId, addressId);
		return ResponseEntity.noContent().build();
	}

	private void assertOwnerOrAdmin(UUID targetUserId, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

		if (principal.isAdmin()) {
			return;
		}

		if (!principal.getUserId().equals(targetUserId)) {
			throw new ForbiddenException("Not allowed");
		}
	}

}
