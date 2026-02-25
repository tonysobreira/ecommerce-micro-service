package com.example.authservice.mapper;

import com.example.authservice.model.UserAccount;
import com.example.authservice.dto.response.MeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

	@Mapping(target = "userId", source = "id")
	@Mapping(target = "roles", expression = "java(toRoles(user.getRoles()))")
	MeResponse toMeResponse(UserAccount user);

	default String[] toRoles(String roles) {
		if (roles == null || roles.isBlank()) {
			return new String[0];
		}
		String[] mappedRoles = roles.split(",");
		for (int i = 0; i < mappedRoles.length; i++) {
			mappedRoles[i] = mappedRoles[i].trim();
		}
		return mappedRoles;
	}

}
