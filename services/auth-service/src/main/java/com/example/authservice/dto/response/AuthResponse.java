package com.example.authservice.dto.response;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
	UUID userId,

	String email,

	Set<String> roles,

	String accessToken,

	String refreshToken
) {

}
