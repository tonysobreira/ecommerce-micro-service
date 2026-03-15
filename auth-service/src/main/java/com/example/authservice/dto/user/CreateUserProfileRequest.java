package com.example.authservice.dto.user;

import java.util.UUID;

public record CreateUserProfileRequest(
	UUID id,
	
	String email
) {

}
