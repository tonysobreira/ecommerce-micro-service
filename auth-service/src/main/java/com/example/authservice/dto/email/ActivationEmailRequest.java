package com.example.authservice.dto.email;

public class ActivationEmailRequest {

	private String email;
	private String activationLink;
	private long expiresInMinutes;

	public ActivationEmailRequest() {
	}

	public ActivationEmailRequest(String email, String activationLink, long expiresInMinutes) {
		this.email = email;
		this.activationLink = activationLink;
		this.expiresInMinutes = expiresInMinutes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationLink() {
		return activationLink;
	}

	public void setActivationLink(String activationLink) {
		this.activationLink = activationLink;
	}

	public long getExpiresInMinutes() {
		return expiresInMinutes;
	}

	public void setExpiresInMinutes(long expiresInMinutes) {
		this.expiresInMinutes = expiresInMinutes;
	}
}
