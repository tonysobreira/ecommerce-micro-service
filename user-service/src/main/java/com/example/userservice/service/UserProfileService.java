package com.example.userservice.service;

import com.example.userservice.domain.UserProfile;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.errors.ConflictException;
import com.example.userservice.errors.NotFoundException;
import com.example.userservice.repo.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService {

	private final UserProfileRepository repo;

	public UserProfileService(UserProfileRepository repo) {
		this.repo = repo;
	}

	@Transactional(readOnly = true)
	public List<UserProfile> listAllActive() {
		return repo.findAll().stream().filter(p -> !p.isDeleted()).toList();
	}

	@Transactional(readOnly = true)
	public UserProfile getActive(UUID id) {
		UserProfile p = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
		if (p.isDeleted()) {
			throw new NotFoundException("User not found");
		}
		return p;
	}

	@Transactional
	public UserProfile update(UUID id, UserUpdateRequest req) {
		UserProfile p = getActive(id);

		if (req.getEmail() != null && !req.getEmail().isBlank()) {
			repo.findByEmailIgnoreCase(req.getEmail()).ifPresent(other -> {
				if (!other.getId().equals(id)) {
					throw new ConflictException("Email already in use");
				}
			});
			p.setEmail(req.getEmail().trim().toLowerCase());
		}

		if (req.getFirstName() != null) {
			p.setFirstName(req.getFirstName());
		}

		if (req.getLastName() != null) {
			p.setLastName(req.getLastName());
		}

		if (req.getPhone() != null) {
			p.setPhone(req.getPhone());
		}

		p.touchUpdated();
		return repo.save(p);
	}

	@Transactional
	public void softDelete(UUID id) {
		UserProfile p = getActive(id);
		p.softDelete();
		repo.save(p);
	}

	/**
	 * Optional helper if you later want auth-service to create profiles on
	 * registration.
	 */
	@Transactional
	public UserProfile createIfMissing(UUID id, String email) {
		return repo.findById(id).orElseGet(() -> {
			Instant now = Instant.now();
			UserProfile p = new UserProfile(id, email, now, now);
			return repo.save(p);
		});
	}

}
