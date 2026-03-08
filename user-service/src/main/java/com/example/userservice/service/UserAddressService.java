package com.example.userservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.dto.request.CreateUserAddressRequest;
import com.example.userservice.dto.request.UpdateUserAddressRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.UserAddress;
import com.example.userservice.repository.UserAddressRepository;

@Service
public class UserAddressService {

	private final UserAddressRepository userAddressRepository;

	private final UserProfileService userProfileService;

	public UserAddressService(UserAddressRepository userAddressRepository, UserProfileService userProfileService) {
		this.userAddressRepository = userAddressRepository;
		this.userProfileService = userProfileService;
	}

	@Transactional(readOnly = true)
	public List<UserAddress> listByUserId(UUID userId) {
		userProfileService.getActive(userId);
		return userAddressRepository.findAllByUserProfileId(userId);
	}

	@Transactional(readOnly = true)
	public UserAddress getById(UUID userId, UUID addressId) {
		userProfileService.getActive(userId);
		return userAddressRepository.findByIdAndUserProfileId(addressId, userId)
				.orElseThrow(() -> new NotFoundException("Address not found"));
	}

	@Transactional
	public UserAddress create(UUID userId, CreateUserAddressRequest request) {
		userProfileService.getActive(userId);
		UserAddress address = new UserAddress(userId, request.line1().trim(), trimToNull(request.line2()),
				request.city().trim(), request.state().trim(), request.zip().trim(), request.country().trim());
		return userAddressRepository.save(address);
	}

	@Transactional
	public UserAddress update(UUID userId, UUID addressId, UpdateUserAddressRequest request) {
		UserAddress address = getById(userId, addressId);
		address.setLine1(request.line1().trim());
		address.setLine2(trimToNull(request.line2()));
		address.setCity(request.city().trim());
		address.setState(request.state().trim());
		address.setZip(request.zip().trim());
		address.setCountry(request.country().trim());
		return userAddressRepository.save(address);
	}

	@Transactional
	public void delete(UUID userId, UUID addressId) {
		UserAddress address = getById(userId, addressId);
		userAddressRepository.delete(address);
	}

	private String trimToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

}
