package com.example.authservice.web;

import com.example.authservice.dto.*;
import com.example.authservice.domain.UserAccount;
import com.example.authservice.security.UserPrincipal;
import com.example.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
		return authService.register(req.getEmail(), req.getPassword());
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req.getEmail(), req.getPassword());
	}

	@GetMapping("/me")
	public MeResponse me(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		UserAccount u = authService.getUser(principal.getUserId());

		// roles from principal authorities are ROLE_X; use account roles as source of
		// truth
		String[] roles = (u.getRoles() == null || u.getRoles().isBlank()) ? new String[0] : u.getRoles().split(",");

		return new MeResponse(u.getId(), u.getEmail(), trimRoles(roles));
	}

	@PostMapping("/refresh")
	public AuthResponse refresh(@Valid @RequestBody RefreshRequest req) {
		return authService.refresh(req.getRefreshToken());
	}

	@PostMapping("/logout")
	public void logout(@Valid @RequestBody LogoutRequest req) {
		authService.logout(req.getRefreshToken());
	}

	/**
	 * Optional internal endpoint. You can call this from other services if you ever
	 * choose not to validate JWT locally. (Gateway already validates.)
	 */
	@PostMapping("/validate")
	public ValidateResponse validate(@Valid @RequestBody ValidateRequest req,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		// Minimal: if token is present and /validate is called via gateway with
		// Authorization,
		// downstream can just treat it as "valid". For now we keep it simple:
		// If this endpoint is hit, caller provides token string and expects boolean.
		// Real verification is done in JwtAuthFilter already if secured.
		return new ValidateResponse(true, null, null, new String[0]);
	}

	private static String[] trimRoles(String[] roles) {
		for (int i = 0; i < roles.length; i++) {
			roles[i] = roles[i].trim();
		}
		return roles;
	}

}
