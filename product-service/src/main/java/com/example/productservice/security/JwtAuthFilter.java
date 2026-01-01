package com.example.productservice.security;

import com.example.productservice.errors.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtVerifier verifier;

	public JwtAuthFilter(JwtVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();

		// Public reads:
		if (request.getMethod().equalsIgnoreCase("GET")
				&& (path.startsWith("/products") || path.startsWith("/categories") || path.startsWith("/product-images")
						|| path.startsWith("/actuator/health"))) {
			filterChain.doFilter(request, response);
			return;
		}

		// Internal endpoints should be protected too
		if (path.startsWith("/actuator/health")) {
			filterChain.doFilter(request, response);
			return;
		}

		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (auth == null || !auth.startsWith("Bearer ")) {
			filterChain.doFilter(request, response); // 401 by security config
			return;
		}

		try {
			Claims claims = verifier.verify(auth.substring(7)).getBody();
			UUID userId = UUID.fromString(claims.getSubject());
			String email = claims.get("email", String.class);

			@SuppressWarnings("unchecked")
			List<String> roles = claims.get("roles", List.class);

			if (roles == null) {
				roles = new ArrayList<>();
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

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		String method = request.getMethod();

		// actuator
		if (path.startsWith("/actuator")) {
			return true;
		}

		// internal calls are protected by network + gateway not routing them
		if (path.startsWith("/internal")) {
			return true;
		}

		// public reads
		if ("GET".equalsIgnoreCase(method) && (path.startsWith("/products") || path.startsWith("/categories")
				|| path.startsWith("/product-images"))) {
			return true;
		}

		return false;
	}

}
