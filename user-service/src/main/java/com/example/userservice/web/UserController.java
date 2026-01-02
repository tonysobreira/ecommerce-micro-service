package com.example.userservice.web;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.errors.ForbiddenException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.security.UserPrincipal;
import com.example.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
		assertOwnerOrAdmin(id, auth);
		return mapper.toResponse(service.getActive(id));
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
