package com.example.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.userservice.dto.response.UserAddressResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.model.UserAddress;
import com.example.userservice.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", source = "id")
	UserResponse toResponse(UserProfile profile);

	@Mapping(target = "id", source = "id")
	UserAddressResponse toResponse(UserAddress profile);

}
