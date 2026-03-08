package com.example.authservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.authservice.model.UserAccount;
import com.example.authservice.repository.UserAccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserAccountRepository userAccountRepository;

	public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserAccount user = userAccountRepository.findByEmailIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new UserPrincipal(user);
	}

}