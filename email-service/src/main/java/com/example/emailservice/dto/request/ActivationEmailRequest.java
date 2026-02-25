package com.example.emailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ActivationEmailRequest {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String activationLink;

	private long expiresInMinutes;

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getActivationLink() { return activationLink; }
	public void setActivationLink(String activationLink) { this.activationLink = activationLink; }
	public long getExpiresInMinutes() { return expiresInMinutes; }
	public void setExpiresInMinutes(long expiresInMinutes) { this.expiresInMinutes = expiresInMinutes; }
}
