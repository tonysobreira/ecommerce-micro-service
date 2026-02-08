package com.example.authservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authservice.domain.UserAccount;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.RegisterResponse;
import com.example.authservice.errors.ConflictException;
import com.example.authservice.errors.NotFoundException;
import com.example.authservice.errors.UnauthorizedException;
import com.example.authservice.repo.UserAccountRepository;
import com.example.authservice.security.JwtIssuer;
import com.example.authservice.security.JwtVerifier;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

	private final UserAccountRepository users;


	private final PasswordEncoder passwordEncoder;

	private final JwtIssuer issuer;

	private final JwtVerifier verifier;

	private final ActivationEmailService activationEmailService;


	private final long activationTtlMinutes;

	public AuthService(UserAccountRepository users,
			PasswordEncoder passwordEncoder, JwtIssuer issuer, JwtVerifier verifier,
			ActivationEmailService activationEmailService,
			@Value("${security.activation.ttl-minutes:30}") long activationTtlMinutes) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
		this.issuer = issuer;
		this.verifier = verifier;
		this.activationEmailService = activationEmailService;
		this.activationTtlMinutes = activationTtlMinutes;
	}

	@Transactional
	public RegisterResponse register(String email, String password) {
		users.findByEmailIgnoreCase(email).ifPresent(u -> {
			throw new ConflictException("Email already registered");
		});

		UUID userId = UUID.randomUUID();
		String passwordHash = passwordEncoder.encode(password);

		// default role USER
		UserAccount account = new UserAccount(userId, email.trim().toLowerCase(Locale.ROOT), passwordHash, "USER",
				Instant.now());
		users.save(account);

		String activationRaw = issuer.issueActivationToken(account.getId(), account.getEmail(),
				activationTtlMinutes * 60L);

		activationEmailService.sendActivationEmail(account.getEmail(), activationRaw);

		return new RegisterResponse("Registration successful. Check your email to activate your account.");
	}

	@Transactional
	public AuthResponse login(String email, String password) {
		UserAccount account = users.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

		if (account.isDeleted()) {
			throw new UnauthorizedException("Account disabled");
		}

		if (!account.isActivated()) {
			throw new UnauthorizedException("Account not activated");
		}

		if (!passwordEncoder.matches(password, account.getPasswordHash())) {
			throw new UnauthorizedException("Invalid credentials");
		}

		return issueTokens(account);
	}

	@Transactional
	public void activateAccount(String rawToken) {
		Claims claims;
		try {
			claims = verifier.verify(rawToken).getBody();
		} catch (Exception ex) {
			throw new UnauthorizedException("Invalid activation token");
		}

		if (!"activation".equals(claims.get("typ", String.class))) {
			throw new UnauthorizedException("Invalid activation token");
		}

		UUID userId = UUID.fromString(claims.getSubject());
		UserAccount account = users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
		account.activate();
		users.save(account);
	}

	@Transactional
	public AuthResponse refresh(String refreshTokenRaw) {
		Claims claims;
		try {
			claims = verifier.verify(refreshTokenRaw).getBody();
		} catch (Exception ex) {
			throw new UnauthorizedException("Invalid refresh token");
		}

		if (!"refresh".equals(claims.get("typ", String.class))) {
			throw new UnauthorizedException("Invalid refresh token");
		}

		UUID userId = UUID.fromString(claims.getSubject());
		UserAccount account = users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

		if (account.isDeleted()) {
			throw new UnauthorizedException("Account disabled");
		}

		return issueTokens(account);
	}

	@Transactional
	public void logout(String refreshTokenRaw) {
		// Stateless refresh tokens do not require server-side revocation.
	}

	public UserAccount getUser(UUID userId) {
		return users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public AuthResponse issueTokens(UserAccount account) {
		List<String> roles = splitRoles(account.getRoles());
		String accessToken = issuer.issueAccessToken(account.getId(), account.getEmail(), roles);
		String refreshToken = issuer.issueRefreshToken(account.getId());

		return new AuthResponse(account.getId(), account.getEmail(), roles.toArray(new String[0]), accessToken,
				refreshToken);
	}

	private static List<String> splitRoles(String rolesCsv) {
		if (rolesCsv == null || rolesCsv.isBlank())
			return List.of();
		String[] parts = rolesCsv.split(",");
		List<String> out = new ArrayList<>();
		for (String p : parts) {
			if (p != null && !p.isBlank()) {
				out.add(p.trim());
			}
		}
		return out;
	}


}