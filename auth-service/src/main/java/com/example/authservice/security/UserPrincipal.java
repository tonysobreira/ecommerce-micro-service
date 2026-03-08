package com.example.authservice.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.authservice.model.UserAccount;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private final UUID userId;

	private final String email;

	private final String passwordHash;

	private final List<SimpleGrantedAuthority> authorities;

	public UserPrincipal(UUID userId, String email, List<String> roles) {
		this.userId = userId;
		this.email = email;
		this.passwordHash = "";
		List<SimpleGrantedAuthority> list = new ArrayList<>();
		for (String r : roles) {
			if (r != null && !r.isBlank()) {
				list.add(new SimpleGrantedAuthority("ROLE_" + r.trim()));
			}
		}
		this.authorities = Collections.unmodifiableList(list);
	}

	public UserPrincipal(UserAccount user) {
		this.userId = user.getId();
		this.email = user.getEmail();
		this.passwordHash = user.getPasswordHash();

		if (user.getRoles() != null && !user.getRoles().isBlank()) {
			List<SimpleGrantedAuthority> list = new ArrayList<>();
			String[] parts = user.getRoles().split(",");
			List<String> out = new ArrayList<>();

			for (String p : parts) {
				if (p != null && !p.isBlank()) {
					out.add(p.trim());
				}
			}

			for (String r : out) {
				if (r != null && !r.isBlank()) {
					list.add(new SimpleGrantedAuthority("ROLE_" + r.trim()));
				}
			}

			this.authorities = Collections.unmodifiableList(list);
		} else {
			this.authorities = Collections.emptyList();
		}
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
		return passwordHash;
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
