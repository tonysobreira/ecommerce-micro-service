package com.example.authservice.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.authservice.exception.UnauthorizedException;
import com.example.authservice.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtVerifier verifier;

	public JwtAuthFilter(JwtVerifier verifier) {
		this.verifier = verifier;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (auth == null || !auth.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Claims claims = verifier.verify(auth.substring(7)).getBody();
			UUID userId = UUID.fromString(claims.getSubject());
			String email = claims.get("email", String.class);

			List<String> roleNames = claims.get("roles", List.class);
			Set<Role> roles = new HashSet<>();
			if (roleNames != null) {
				for (String roleName : roleNames) {
					roles.add(new Role(roleName));
				}
			}

			UserPrincipal principal = new UserPrincipal(userId, email == null ? "" : email, roles);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
					null, principal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("Invalid or expired token");
		}
	}

}
