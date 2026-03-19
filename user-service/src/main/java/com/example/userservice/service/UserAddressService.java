package com.example.userservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.dto.request.CreateUserAddressRequest;
import com.example.userservice.dto.request.UpdateUserAddressRequest;
import com.example.userservice.dto.response.UserAddressResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.UserAddress;
import com.example.userservice.repository.UserAddressRepository;

@Service
public class UserAddressService {

	private final UserAddressRepository userAddressRepository;

	private final UserProfileService userProfileService;

	private final UserMapper mapper;

	public UserAddressService(UserAddressRepository userAddressRepository, UserProfileService userProfileService,
			UserMapper mapper) {
		this.userAddressRepository = userAddressRepository;
		this.userProfileService = userProfileService;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<UserAddressResponse> listByUserId(UUID userId) {
		userProfileService.getActive(userId);
		return userAddressRepository.findAllByUserProfileId(userId).stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UserAddressResponse getById(UUID userId, UUID addressId) {
		userProfileService.getActive(userId);
		UserAddress ua = userAddressRepository.findByIdAndUserProfileId(addressId, userId)
				.orElseThrow(() -> new NotFoundException("Address not found"));
		return mapper.toResponse(ua);
	}

	@Transactional
	public UserAddressResponse create(UUID userId, CreateUserAddressRequest request) {
		userProfileService.getActive(userId);
		UserAddress address = new UserAddress(userId, request.line1().trim(), trimToNull(request.line2()),
				request.city().trim(), request.state().trim(), request.zip().trim(), request.country().trim());
		return mapper.toResponse(userAddressRepository.save(address));
	}

	@Transactional
	public UserAddressResponse update(UUID userId, UUID addressId, UpdateUserAddressRequest request) {
		UserAddress address = findById(userId, addressId);
		address.setLine1(request.line1().trim());
		address.setLine2(trimToNull(request.line2()));
		address.setCity(request.city().trim());
		address.setState(request.state().trim());
		address.setZip(request.zip().trim());
		address.setCountry(request.country().trim());
		return mapper.toResponse(userAddressRepository.save(address));
	}

	@Transactional
	public void delete(UUID userId, UUID addressId) {
		UserAddress address = findById(userId, addressId);
		userAddressRepository.delete(address);
	}

	@Transactional(readOnly = true)
	public UserAddress findById(UUID userId, UUID addressId) {
		userProfileService.getActive(userId);
		return userAddressRepository.findByIdAndUserProfileId(addressId, userId)
				.orElseThrow(() -> new NotFoundException("Address not found"));
	}

	private String trimToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

}
