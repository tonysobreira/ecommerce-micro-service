package com.example.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
	@Email
	String email,

	@Size(max = 80)
	String firstName,

	@Size(max = 80)
	String lastName,

	@Size(max = 40)
	String phone
) {

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

}
