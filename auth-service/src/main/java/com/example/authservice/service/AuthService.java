package com.example.authservice.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authservice.client.UserClient;
import com.example.authservice.dto.response.AuthResponse;
import com.example.authservice.dto.response.RegisterResponse;
import com.example.authservice.exception.ConflictException;
import com.example.authservice.exception.NotFoundException;
import com.example.authservice.exception.UnauthorizedException;
import com.example.authservice.model.RefreshToken;
import com.example.authservice.model.Role;
import com.example.authservice.model.UserAccount;
import com.example.authservice.repository.RefreshTokenRepository;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserAccountRepository;
import com.example.authservice.security.JwtIssuer;
import com.example.authservice.security.JwtVerifier;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

	private final UserAccountRepository userAccountRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final RoleRepository roleRepository;

	private final AuthenticationManager authManager;

	private final PasswordEncoder passwordEncoder;

	private final JwtIssuer issuer;

	private final JwtVerifier verifier;

	private final ActivationEmailService activationEmailService;

	private final long refreshTtlDays;

	private final long activationTtlMinutes;

	private final UserClient userClient;

	public AuthService(UserAccountRepository userAccountRepository, RefreshTokenRepository refreshTokenRepository,
			RoleRepository roleRepository, AuthenticationManager authManager, PasswordEncoder passwordEncoder,
			JwtIssuer issuer, JwtVerifier verifier, ActivationEmailService activationEmailService,
			@Value("${security.jwt.refresh-ttl-days}") long refreshTtlDays,
			@Value("${security.activation.ttl-minutes:30}") long activationTtlMinutes, UserClient userClient) {
		this.userAccountRepository = userAccountRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.roleRepository = roleRepository;
		this.authManager = authManager;
		this.passwordEncoder = passwordEncoder;
		this.issuer = issuer;
		this.verifier = verifier;
		this.activationEmailService = activationEmailService;
		this.refreshTtlDays = refreshTtlDays;
		this.activationTtlMinutes = activationTtlMinutes;
		this.userClient = userClient;
	}

	@Transactional
	public RegisterResponse register(String email, String password) {
		userAccountRepository.findByEmailIgnoreCase(email).ifPresent(u -> {
			throw new ConflictException("Email already registered");
		});

		UUID userId = UUID.randomUUID();
		String passwordHash = passwordEncoder.encode(password);

		// default role USER
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new NotFoundException("Default role ROLE_USER not found")));

		UserAccount account = new UserAccount(userId, email.trim().toLowerCase(Locale.ROOT), passwordHash, roles,
				Instant.now());
		userAccountRepository.save(account);

		String activationRaw = issuer.issueActivationToken(account.getId(), account.getEmail(),
				activationTtlMinutes * 60L);

		activationEmailService.sendActivationEmail(account.getEmail(), activationRaw);

		return new RegisterResponse("Registration successful. Check your email to activate your account.");
	}

	@Transactional
	public AuthResponse login(String email, String password) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}

		UserAccount account = userAccountRepository.findByEmailIgnoreCase(email).orElseThrow();

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
		UserAccount account = userAccountRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found"));
		account.activate();

		// Create user profile
		

		userAccountRepository.save(account);
	}

	@Transactional
	public AuthResponse refresh(String refreshTokenRaw) {
		String hash = TokenHash.sha256Base64(refreshTokenRaw);
		RefreshToken rt = refreshTokenRepository.findByTokenHash(hash)
				.orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

		if (rt.isRevoked() || rt.isExpired()) {
			throw new UnauthorizedException("Refresh token expired or revoked");
		}

		UserAccount account = userAccountRepository.findById(rt.getUserId())
				.orElseThrow(() -> new NotFoundException("User not found"));

		if (account.isDeleted()) {
			throw new UnauthorizedException("Account disabled");
		}

		// Rotate refresh token: revoke old, issue new
		rt.revoke();
		refreshTokenRepository.save(rt);

		return issueTokens(account);
	}

	@Transactional
	public void logout(String refreshTokenRaw) {
		String hash = TokenHash.sha256Base64(refreshTokenRaw);
		refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> {
			if (!rt.isRevoked()) {
				rt.revoke();
				refreshTokenRepository.save(rt);
			}
		});
	}

	public UserAccount getUser(UUID userId) {
		return userAccountRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public AuthResponse issueTokens(UserAccount account) {
		String accessToken = issuer.issueAccessToken(account.getId(), account.getEmail(), account.getRoles());

		String refreshRaw = generateSecureToken();
		String refreshHash = TokenHash.sha256Base64(refreshRaw);

		Instant now = Instant.now();
		Instant expires = now.plus(refreshTtlDays, ChronoUnit.DAYS);

		RefreshToken rt = new RefreshToken(UUID.randomUUID(), account.getId(), refreshHash, expires, now);
		refreshTokenRepository.save(rt);

		Set<String> roleNames = account.getRoles().stream().map(Role::getName)
				.collect(java.util.stream.Collectors.toSet());
		return new AuthResponse(account.getId(), account.getEmail(), roleNames, accessToken, refreshRaw);
	}

	private static String generateSecureToken() {
		byte[] buf = new byte[48];
		new SecureRandom().nextBytes(buf);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
	}

}
