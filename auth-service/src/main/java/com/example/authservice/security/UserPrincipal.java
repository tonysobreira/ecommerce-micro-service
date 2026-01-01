package com.example.authservice.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
				list.add(new SimpleGrantedAuthority("ROLE_" + r.trim()));
			}
		}
		this.authorities = Collections.unmodifiableList(list);
	}

	public UUID getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
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

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
