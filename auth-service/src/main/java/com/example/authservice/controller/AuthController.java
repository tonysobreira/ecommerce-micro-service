package com.example.authservice.controller;

import com.example.authservice.model.UserAccount;
import com.example.authservice.dto.request.*;
import com.example.authservice.dto.response.*;
import com.example.authservice.mapper.AuthMapper;
import com.example.authservice.security.UserPrincipal;
import com.example.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req.email(), req.password()));
	}

	@GetMapping("/activate")
	public ResponseEntity<RegisterResponse> activate(@RequestParam("token") String token) {
		authService.activateAccount(token);
		return ResponseEntity.ok(new RegisterResponse("Account activated successfully. You can now log in."));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req.email(), req.password()));
	}

	@GetMapping("/me")
	public ResponseEntity<MeResponse> me(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		UserAccount u = authService.getUser(principal.getUserId());
		return ResponseEntity.ok(authMapper.toMeResponse(u));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest req) {
		return ResponseEntity.ok(authService.refresh(req.refreshToken()));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest req) {
		authService.logout(req.refreshToken());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/validate")
	public ResponseEntity<ValidateResponse> validate(@Valid @RequestBody ValidateRequest req,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		return ResponseEntity.ok(new ValidateResponse(true, null, null, new String[0]));
	}

}
