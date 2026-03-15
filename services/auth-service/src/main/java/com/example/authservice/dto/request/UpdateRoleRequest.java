package com.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateRoleRequest(
	@NotBlank(message = "Name is required")
	@Size(max = 50, message = "Name must have at most 50 characters")
	@Pattern(regexp = "^ROLE_[A-Z0-9_]+$", message = "Name must match ROLE_[A-Z0-9_]+")
	String name
) {

}
