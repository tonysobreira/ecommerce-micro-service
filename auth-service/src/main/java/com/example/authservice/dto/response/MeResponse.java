package com.example.authservice.dto.response;

import java.util.UUID;

public record MeResponse(
	UUID userId,

	String email,

	String[] roles
) {

}
