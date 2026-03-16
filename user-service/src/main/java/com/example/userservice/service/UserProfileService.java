package com.example.userservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.dto.request.UserUpdateRequest;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.exception.ConflictException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.UserProfile;
import com.example.userservice.repository.UserProfileRepository;
import com.example.userservice.security.UserPrincipal;

@Service
public class UserProfileService {

	private final UserProfileRepository userProfileRepository;

	private final UserMapper mapper;

	public UserProfileService(UserProfileRepository userProfileRepository, UserMapper mapper) {
		this.userProfileRepository = userProfileRepository;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<UserResponse> listAllActive() {
		List<UserProfile> list = userProfileRepository.findAll().stream().filter(p -> p.getDeletedAt() == null)
				.toList();

		return list.stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UserResponse getActive(UUID id) {
		UserProfile p = findByIdActive(id);

		if (p.getDeletedAt() != null) {
			throw new NotFoundException("User not found");
		}

		return mapper.toResponse(p);
	}

	@Transactional
	public UserResponse update(UUID id, UserUpdateRequest req) {
		UserProfile p = findByIdActive(id);

		if (req.email() != null && !req.email().isBlank()) {
			userProfileRepository.findByEmailIgnoreCase(req.email()).ifPresent(other -> {
				if (!other.getId().equals(id)) {
					throw new ConflictException("Email already in use");
				}
			});
			p.setEmail(req.email().trim().toLowerCase());
		}

		if (req.firstName() != null) {
			p.setFirstName(req.firstName());
		}

		if (req.lastName() != null) {
			p.setLastName(req.lastName());
		}

		if (req.phone() != null) {
			p.setPhone(req.phone());
		}

		return mapper.toResponse(userProfileRepository.save(p));
	}

	@Transactional
	public void softDelete(UUID id) {
		UserProfile p = findByIdActive(id);
		p.setDeletedAt(Instant.now());
		userProfileRepository.save(p);
	}

	/**
	 * Optional helper if you later want auth-service to create profiles on
	 * activation.
	 */
	@Transactional
	public UserResponse createIfMissing(UserPrincipal principal) {
		UserProfile userProfile = userProfileRepository.findById(principal.getUserId()).orElseGet(() -> {
			UserProfile p = new UserProfile(principal.getUserId(), principal.getEmail());
			return userProfileRepository.save(p);
		});
		return mapper.toResponse(userProfile);
	}

	@Transactional
	public UserResponse createIfMissing(UUID id, String email) {
		UserProfile userProfile = userProfileRepository.findById(id).orElseGet(() -> {
			UserProfile p = new UserProfile(id, email.trim().toLowerCase());
			return userProfileRepository.save(p);
		});
		return mapper.toResponse(userProfile);
	}

	public UserResponse getById(UUID id) {
		return mapper.toResponse(findById(id));
	}

	public UserProfile findById(UUID id) {
		return userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public UserProfile findByIdActive(UUID id) {
		return userProfileRepository.findById(id).filter(p -> p.getDeletedAt() == null)
				.orElseThrow(() -> new NotFoundException("User not found"));
	}

}
