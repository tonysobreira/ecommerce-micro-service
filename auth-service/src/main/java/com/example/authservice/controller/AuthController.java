package com.example.authservice.controller;

import com.example.authservice.model.UserAccount;
import com.example.authservice.dto.request.*;
import com.example.authservice.dto.response.*;
import com.example.authservice.mapper.AuthMapper;
import com.example.authservice.security.UserPrincipal;
import com.example.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	private final AuthMapper authMapper;

	public AuthController(AuthService authService, AuthMapper authMapper) {
		this.authService = authService;
		this.authMapper = authMapper;
	}

	@PostMapping("/register")
	public RegisterResponse register(@Valid @RequestBody RegisterRequest req) {
		return authService.register(req.email(), req.password());
	}

	@GetMapping("/activate")
	public RegisterResponse activate(@RequestParam("token") String token) {
		authService.activateAccount(token);
		return new RegisterResponse("Account activated successfully. You can now log in.");
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req.email(), req.password());
	}

	@GetMapping("/me")
	public MeResponse me(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		UserAccount u = authService.getUser(principal.getUserId());
		return authMapper.toMeResponse(u);
	}

	@PostMapping("/refresh")
	public AuthResponse refresh(@Valid @RequestBody RefreshRequest req) {
		return authService.refresh(req.refreshToken());
	}

	@PostMapping("/logout")
	public void logout(@Valid @RequestBody LogoutRequest req) {
		authService.logout(req.refreshToken());
	}

	@PostMapping("/validate")
	public ValidateResponse validate(@Valid @RequestBody ValidateRequest req,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		return new ValidateResponse(true, null, null, new String[0]);
	}

}
