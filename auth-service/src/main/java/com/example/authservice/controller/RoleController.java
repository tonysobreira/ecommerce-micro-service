package com.example.authservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.request.CreateRoleRequest;
import com.example.authservice.dto.request.UpdateRoleRequest;
import com.example.authservice.dto.response.RoleResponse;
import com.example.authservice.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping
	public List<RoleResponse> findAll() {
		return roleService.findAll();
	}

	@GetMapping("/{id}")
	public RoleResponse findById(@PathVariable UUID id) {
		return roleService.findById(id);
	}

	@PostMapping
	public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
		return roleService.create(request);
	}

	@PutMapping("/{id}")
	public RoleResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateRoleRequest request) {
		return roleService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		roleService.delete(id);
	}

}
