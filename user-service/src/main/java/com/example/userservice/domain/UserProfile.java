package com.example.userservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@Column(nullable = false)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String phone;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	public UserProfile() {
	}

	public UserProfile(UUID id, String email, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void touchUpdated() {
		this.updatedAt = Instant.now();
	}

	public void softDelete() {
		this.deletedAt = Instant.now();
		this.touchUpdated();
	}

}
