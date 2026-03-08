package com.example.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.authservice.dto.response.MeResponse;
import com.example.authservice.model.UserAccount;

@Mapper(componentModel = "spring")
public interface AuthMapper {

	@Mapping(target = "userId", source = "id")
	MeResponse toMeResponse(UserAccount user);

}
