package com.example.authservice.dto.response;

import java.util.UUID;

public record AuthResponse(
	UUID userId,

	String email,

	String[] roles,

	String accessToken,

	String refreshToken
) {

}
