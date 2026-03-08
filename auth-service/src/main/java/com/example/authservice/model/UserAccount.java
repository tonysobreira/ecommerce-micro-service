package com.example.authservice.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccount {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>();

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "activated_at")
	private Instant activatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	protected UserAccount() {
	}

	public UserAccount(UUID id, String email, String passwordHash, Set<Role> roles, Instant createdAt) {
		this.id = id;
		this.email = email;
		this.passwordHash = passwordHash;
		this.roles = roles;
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public Instant getActivatedAt() {
		return activatedAt;
	}

	public boolean isActivated() {
		return activatedAt != null;
	}

	public void activate() {
		if (activatedAt == null) {
			activatedAt = Instant.now();
		}
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}

	public void softDelete() {
		this.deletedAt = Instant.now();
	}

}
