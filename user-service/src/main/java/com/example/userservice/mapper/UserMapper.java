package com.example.userservice.mapper;

import com.example.userservice.model.UserProfile;
import com.example.userservice.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", source = "id")
	UserResponse toResponse(UserProfile profile);

}
