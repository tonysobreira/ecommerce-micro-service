package com.example.authservice.service;

import com.example.authservice.domain.ActivationToken;
import com.example.authservice.domain.RefreshToken;
import com.example.authservice.domain.UserAccount;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.RegisterResponse;
import com.example.authservice.errors.ConflictException;
import com.example.authservice.errors.NotFoundException;
import com.example.authservice.errors.UnauthorizedException;
import com.example.authservice.repo.ActivationTokenRepository;
import com.example.authservice.repo.RefreshTokenRepository;
import com.example.authservice.repo.UserAccountRepository;
import com.example.authservice.security.JwtIssuer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AuthService {

	private final UserAccountRepository users;

	private final RefreshTokenRepository refreshTokens;

	private final ActivationTokenRepository activationTokens;

	private final PasswordEncoder passwordEncoder;

	private final JwtIssuer issuer;

	private final ActivationEmailService activationEmailService;

	private final long refreshTtlDays;

	private final long activationTtlHours;

	public AuthService(UserAccountRepository users, RefreshTokenRepository refreshTokens,
			ActivationTokenRepository activationTokens, PasswordEncoder passwordEncoder, JwtIssuer issuer,
			ActivationEmailService activationEmailService, @Value("${security.jwt.refresh-ttl-days}") long refreshTtlDays,
			@Value("${security.activation.ttl-hours:24}") long activationTtlHours) {
		this.users = users;
		this.refreshTokens = refreshTokens;
		this.activationTokens = activationTokens;
		this.passwordEncoder = passwordEncoder;
		this.issuer = issuer;
		this.activationEmailService = activationEmailService;
		this.refreshTtlDays = refreshTtlDays;
		this.activationTtlHours = activationTtlHours;
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

		String activationRaw = generateSecureToken();
		String activationHash = TokenHash.sha256Base64(activationRaw);
		Instant now = Instant.now();
		ActivationToken token = new ActivationToken(UUID.randomUUID(), account.getId(), activationHash,
				now.plus(activationTtlHours, ChronoUnit.HOURS), now);
		activationTokens.save(token);

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
		String hash = TokenHash.sha256Base64(rawToken);
		ActivationToken token = activationTokens.findByTokenHash(hash)
				.orElseThrow(() -> new UnauthorizedException("Invalid activation token"));

		if (token.isUsed() || token.isExpired()) {
			throw new UnauthorizedException("Activation token expired or already used");
		}

		UserAccount account = users.findById(token.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
		account.activate();
		users.save(account);

		token.markUsed();
		activationTokens.save(token);
	}

	@Transactional
	public AuthResponse refresh(String refreshTokenRaw) {
		String hash = TokenHash.sha256Base64(refreshTokenRaw);
		RefreshToken rt = refreshTokens.findByTokenHash(hash)
				.orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

		if (rt.isRevoked() || rt.isExpired()) {
			throw new UnauthorizedException("Refresh token expired or revoked");
		}

		UserAccount account = users.findById(rt.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

		if (account.isDeleted()) {
			throw new UnauthorizedException("Account disabled");
		}

		// Rotate refresh token: revoke old, issue new
		rt.revoke();
		refreshTokens.save(rt);

		return issueTokens(account);
	}

	@Transactional
	public void logout(String refreshTokenRaw) {
		String hash = TokenHash.sha256Base64(refreshTokenRaw);
		refreshTokens.findByTokenHash(hash).ifPresent(rt -> {
			if (!rt.isRevoked()) {
				rt.revoke();
				refreshTokens.save(rt);
			}
		});
	}

	public UserAccount getUser(UUID userId) {
		return users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public AuthResponse issueTokens(UserAccount account) {
		List<String> roles = splitRoles(account.getRoles());
		String accessToken = issuer.issueAccessToken(account.getId(), account.getEmail(), roles);

		String refreshRaw = generateSecureToken();
		String refreshHash = TokenHash.sha256Base64(refreshRaw);

		Instant now = Instant.now();
		Instant expires = now.plus(refreshTtlDays, ChronoUnit.DAYS);

		RefreshToken rt = new RefreshToken(UUID.randomUUID(), account.getId(), refreshHash, expires, now);
		refreshTokens.save(rt);

		return new AuthResponse(account.getId(), account.getEmail(), roles.toArray(new String[0]), accessToken,
				refreshRaw);
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

	private static String generateSecureToken() {
		byte[] buf = new byte[48];
		new SecureRandom().nextBytes(buf);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
	}

}
