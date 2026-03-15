package com.example.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userservice.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

	List<UserAddress> findAllByUserProfileId(UUID userProfileId);

	Optional<UserAddress> findByIdAndUserProfileId(UUID id, UUID userProfileId);

}
