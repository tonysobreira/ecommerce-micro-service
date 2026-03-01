package com.example.authservice.dto.response;

import java.util.UUID;

public record ValidateResponse(
	boolean valid,

	UUID userId,

	String email,

	String[] roles
) {

}
