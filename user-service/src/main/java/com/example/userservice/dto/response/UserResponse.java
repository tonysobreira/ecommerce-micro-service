package com.example.userservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
	UUID id,

	String email,

	String firstName,

	String lastName,

	String phone,

	Instant createdAt,

	Instant updatedAt
) {

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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
