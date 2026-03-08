package com.example.authservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authservice.dto.request.CreateRoleRequest;
import com.example.authservice.dto.request.UpdateRoleRequest;
import com.example.authservice.dto.response.RoleResponse;
import com.example.authservice.exception.ConflictException;
import com.example.authservice.exception.NotFoundException;
import com.example.authservice.model.Role;
import com.example.authservice.repository.RoleRepository;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Transactional(readOnly = true)
	public List<RoleResponse> findAll() {
		return roleRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public RoleResponse findById(UUID id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
		return toResponse(role);
	}

	@Transactional
	public RoleResponse create(CreateRoleRequest request) {
		String normalizedName = request.name().trim().toUpperCase();
		if (roleRepository.findByName(normalizedName).isPresent()) {
			throw new ConflictException("Role name already exists");
		}

		Role role = new Role(normalizedName);
		roleRepository.save(role);
		return toResponse(role);
	}

	@Transactional
	public RoleResponse update(UUID id, UpdateRoleRequest request) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
		String normalizedName = request.name().trim().toUpperCase();

		roleRepository.findByName(normalizedName).ifPresent(existing -> {
			if (!existing.getId().equals(id)) {
				throw new ConflictException("Role name already exists");
			}
		});

		role.setName(normalizedName);
		roleRepository.save(role);
		return toResponse(role);
	}

	@Transactional
	public void delete(UUID id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
		long activeAssociations = roleRepository.countActiveUserAssociations(id);
		if (activeAssociations > 0) {
			throw new ConflictException("Cannot delete role with active user associations");
		}
		roleRepository.delete(role);
	}

	private RoleResponse toResponse(Role role) {
		return new RoleResponse(role.getId(), role.getName());
	}

}
