package com.example.userservice.controller;

import java.util.List;
import java.util.UUID;

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
import com.example.userservice.model.UserAddress;
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
	public List<UserAddressResponse> list(@PathVariable UUID userId, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return userAddressService.listByUserId(userId).stream().map(this::toResponse).toList();
	}

	@GetMapping("/{addressId}")
	public UserAddressResponse get(@PathVariable UUID userId, @PathVariable UUID addressId, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return toResponse(userAddressService.getById(userId, addressId));
	}

	@PostMapping
	public UserAddressResponse create(@PathVariable UUID userId, @Valid @RequestBody CreateUserAddressRequest request,
			Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return toResponse(userAddressService.create(userId, request));
	}

	@PutMapping("/{addressId}")
	public UserAddressResponse update(@PathVariable UUID userId, @PathVariable UUID addressId,
			@Valid @RequestBody UpdateUserAddressRequest request, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		return toResponse(userAddressService.update(userId, addressId, request));
	}

	@DeleteMapping("/{addressId}")
	public void delete(@PathVariable UUID userId, @PathVariable UUID addressId, Authentication auth) {
		assertOwnerOrAdmin(userId, auth);
		userAddressService.delete(userId, addressId);
	}

	private UserAddressResponse toResponse(UserAddress address) {
		return new UserAddressResponse(address.getId(), address.getUserProfileId(), address.getLine1(), address.getLine2(),
				address.getCity(), address.getState(), address.getZip(), address.getCountry(), address.getCreatedAt(),
				address.getUpdatedAt());
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
