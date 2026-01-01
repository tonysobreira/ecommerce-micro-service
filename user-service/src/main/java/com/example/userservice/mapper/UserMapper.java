package com.example.userservice.mapper;

import com.example.userservice.domain.UserProfile;
import com.example.userservice.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", source = "id")
	UserResponse toResponse(UserProfile profile);

}
