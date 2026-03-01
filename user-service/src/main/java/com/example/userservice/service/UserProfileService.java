package com.example.userservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.model.UserProfile;
import com.example.userservice.dto.request.UserUpdateRequest;
import com.example.userservice.exception.ConflictException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.repository.UserProfileRepository;
import com.example.userservice.security.UserPrincipal;

@Service
public class UserProfileService {

	private final UserProfileRepository userProfileRepository;

	public UserProfileService(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	@Transactional(readOnly = true)
	public List<UserProfile> listAllActive() {
		return userProfileRepository.findAll().stream().filter(p -> !p.isDeleted()).toList();
	}

	@Transactional(readOnly = true)
	public UserProfile getActive(UUID id) {
		UserProfile p = userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
		if (p.isDeleted()) {
			throw new NotFoundException("User not found");
		}
		return p;
	}

	@Transactional
	public UserProfile update(UUID id, UserUpdateRequest req) {
		UserProfile p = getActive(id);

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

		p.touchUpdated();
		return userProfileRepository.save(p);
	}

	@Transactional
	public void softDelete(UUID id) {
		UserProfile p = getActive(id);
		p.softDelete();
		userProfileRepository.save(p);
	}

	/**
	 * Optional helper if you later want auth-service to create profiles on
	 * registration.
	 */
	@Transactional
	public UserProfile createIfMissing(UUID id, String email) {
		return userProfileRepository.findById(id).orElseGet(() -> {
			Instant now = Instant.now();
			UserProfile p = new UserProfile(id, email, now, now);
			return userProfileRepository.save(p);
		});
	}

	@Transactional
	public UserProfile getOrCreate(UUID id, UserPrincipal principal) {
		return userProfileRepository.findById(id).orElseGet(() -> {
			// only owner (or admin) can auto-create
			if (!principal.isAdmin() && !principal.getUserId().equals(id)) {
				throw new NotFoundException("User not found");
			}

			UserProfile p = new UserProfile();
			p.setId(id);
			p.setEmail(principal.getEmail());
			p.setFirstName(""); // default
			p.setPhone(null);
			p.setCreatedAt(Instant.now());
			p.setUpdatedAt(Instant.now());
			p.setDeletedAt(null);
			return userProfileRepository.save(p);
		});
	}

	public UserProfile findById(UUID id) {
		return userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
	}

}
