package com.example.userservice.web;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.domain.UserProfile;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.errors.ForbiddenException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.security.UserPrincipal;
import com.example.userservice.service.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserProfileService service;
	private final UserMapper mapper;

	public UserController(UserProfileService service, UserMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public List<UserResponse> listAll() {
		return service.listAllActive().stream().map(mapper::toResponse).toList();
	}

	@GetMapping("/{id}")
	public UserResponse getById(@PathVariable("id") UUID id, Authentication auth) {
//		assertOwnerOrAdmin(id, auth);
//		return mapper.toResponse(service.getActive(id));

		UserPrincipal p = (UserPrincipal) auth.getPrincipal();

		// enforce owner/admin access
		if (!p.isAdmin() && !p.getUserId().equals(id)) {
			throw new ForbiddenException("Not allowed");
		}

		UserProfile profile = service.getOrCreate(id, p);
		return mapper.toResponse(profile);
	}

	@GetMapping("/me")
	public UserResponse me(Authentication auth) {
		UserPrincipal p = (UserPrincipal) auth.getPrincipal();
		return mapper.toResponse(service.getOrCreate(p.getUserId(), p));
	}

	@PutMapping("/{id}")
	public UserResponse update(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdateRequest req,
			Authentication auth) {
		assertOwnerOrAdmin(id, auth);
		return mapper.toResponse(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") UUID id, Authentication auth) {
		assertOwnerOrAdmin(id, auth);
		service.softDelete(id);
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
