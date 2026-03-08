package com.example.authservice.dto.response;

import java.util.Set;
import java.util.UUID;

import com.example.authservice.model.Role;

public record AuthResponse(
	UUID userId,

	String email,

	Set<Role> roles,

	String accessToken,

	String refreshToken
) {

}
