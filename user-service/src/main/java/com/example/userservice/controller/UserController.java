package com.example.userservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.request.UserUpdateRequest;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.exception.ForbiddenException;
import com.example.userservice.security.UserPrincipal;
import com.example.userservice.service.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserProfileService service;

	public UserController(UserProfileService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> listAll() {
		return ResponseEntity.ok(service.listAllActive());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getById(@PathVariable("id") UUID id, Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();

		if (!p.isAdmin() && !p.getUserId().equals(id)) {
			throw new ForbiddenException("Not allowed");
		}

		return ResponseEntity.ok(service.createIfMissing(p));
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> me(Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return ResponseEntity.ok(service.createIfMissing(p));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdateRequest req,
			Authentication auth) {
		assertOwnerOrAdmin(id, auth);
		return ResponseEntity.ok(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id, Authentication auth) {
		assertOwnerOrAdmin(id, auth);
		service.softDelete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(service.getById(id));
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
