package com.example.cartservice.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private final UUID userId;

	private final String email;

	private final List<SimpleGrantedAuthority> authorities;

	public UserPrincipal(UUID userId, String email, List<String> roles) {
		this.userId = userId;
		this.email = email;
		List<SimpleGrantedAuthority> list = new ArrayList<>();
		for (String r : roles) {
			if (r != null && !r.isBlank()) {
				list.add(new SimpleGrantedAuthority(r.trim()));
			}
		}
		this.authorities = Collections.unmodifiableList(list);
	}

	public UUID getUserId() {
		return userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return email;
	}

}
