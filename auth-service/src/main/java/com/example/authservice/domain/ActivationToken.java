package com.example.authservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activation_tokens")
public class ActivationToken {

	@Id
	private UUID id;

	@Column(name = "user_id", nullable = false, unique = true)
	private UUID userId;

	@Column(name = "token_hash", nullable = false, unique = true)
	private String tokenHash;

	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "used_at")
	private Instant usedAt;

	protected ActivationToken() {
	}

	public ActivationToken(UUID id, UUID userId, String tokenHash, Instant expiresAt, Instant createdAt) {
		this.id = id;
		this.userId = userId;
		this.tokenHash = tokenHash;
		this.expiresAt = expiresAt;
		this.createdAt = createdAt;
	}

	public UUID getUserId() {
		return userId;
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiresAt);
	}

	public boolean isUsed() {
		return usedAt != null;
	}

	public void markUsed() {
		this.usedAt = Instant.now();
	}
}
