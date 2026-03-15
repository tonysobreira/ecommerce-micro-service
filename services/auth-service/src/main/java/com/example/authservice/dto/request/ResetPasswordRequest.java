package com.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
	@NotBlank 
	String token,

	@NotBlank 
	@Size(min = 4, max = 100) 
	String newPassword,

	@NotBlank 
	@Size(min = 4, max = 100) 
	String repeatPassword
) {

}
