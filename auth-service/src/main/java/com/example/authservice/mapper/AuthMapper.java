package com.example.authservice.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.authservice.dto.response.MeResponse;
import com.example.authservice.dto.response.RoleResponse;
import com.example.authservice.model.Role;
import com.example.authservice.model.UserAccount;

@Mapper(componentModel = "spring")
public interface AuthMapper {

	@Mapping(target = "userId", source = "id")
	@Mapping(target = "roles", source = "roles")
	MeResponse toMeResponse(UserAccount user);

	default String[] toRoleNames(Set<Role> roles) {
		return roles.stream().map(Role::getName).toArray(String[]::new);
	}

	RoleResponse toResponse(Role role);

}
