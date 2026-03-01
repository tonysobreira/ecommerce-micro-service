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

}
