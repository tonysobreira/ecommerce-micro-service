package com.example.authservice.mapper;

import org.mapstruct.Mapper;

import com.example.authservice.dto.response.RoleResponse;
import com.example.authservice.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	RoleResponse toResponse(Role role);

}
