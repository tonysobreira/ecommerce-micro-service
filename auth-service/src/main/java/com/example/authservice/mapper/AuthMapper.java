package com.example.authservice.mapper;

import com.example.authservice.domain.UserAccount;
import com.example.authservice.dto.response.MeResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

	public MeResponse toMeResponse(UserAccount user) {
		String[] roles = (user.getRoles() == null || user.getRoles().isBlank()) ? new String[0] : user.getRoles().split(",");
		for (int i = 0; i < roles.length; i++) {
			roles[i] = roles[i].trim();
		}
		return new MeResponse(user.getId(), user.getEmail(), roles);
	}
}
