package com.example.authservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.request.ForgotPasswordRequest;
import com.example.authservice.dto.request.LoginRequest;
import com.example.authservice.dto.request.LogoutRequest;
import com.example.authservice.dto.request.RefreshRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.request.ResendActivationRequest;
import com.example.authservice.dto.request.ResetPasswordRequest;
import com.example.authservice.dto.request.ValidateRequest;
import com.example.authservice.dto.response.AuthResponse;
import com.example.authservice.dto.response.MeResponse;
import com.example.authservice.dto.response.RegisterResponse;
import com.example.authservice.dto.response.ValidateResponse;
import com.example.authservice.security.UserPrincipal;
import com.example.authservice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
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

	@PostMapping("/activation/resend")
	public ResponseEntity<RegisterResponse> resendActivation(@Valid @RequestBody ResendActivationRequest req) {
		return ResponseEntity.ok(authService.resendActivation(req.email()));
	}

	@PostMapping("/password/forgot")
	public ResponseEntity<RegisterResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
		return ResponseEntity.ok(authService.forgotPassword(req.email()));
	}

	@PostMapping("/password/reset")
	public ResponseEntity<RegisterResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
		return ResponseEntity.ok(authService.resetPassword(req.token(), req.newPassword(), req.repeatPassword()));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req.email(), req.password()));
	}

	@GetMapping("/me")
	public ResponseEntity<MeResponse> me(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		return ResponseEntity.ok(authService.getUser(principal.getUserId()));
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
