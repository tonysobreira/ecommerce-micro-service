package com.example.authservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<RoleResponse>> findAll() {
		return ResponseEntity.ok(roleService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleResponse> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(roleService.findById(id));
	}

	@PostMapping
	public ResponseEntity<RoleResponse> create(@Valid @RequestBody CreateRoleRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateRoleRequest request) {
		return ResponseEntity.ok(roleService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		roleService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
