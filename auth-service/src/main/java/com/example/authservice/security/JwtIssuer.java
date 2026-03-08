package com.example.authservice.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.example.authservice.model.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtIssuer {

	private final SecretKey key;

	private final String issuer;

	private final long accessTtlSeconds;

	public JwtIssuer(String secret, String issuer, long accessTtlSeconds) {
		if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
			throw new IllegalStateException("JWT_SECRET must be at least 32 bytes for HS256");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.issuer = issuer;
		this.accessTtlSeconds = accessTtlSeconds;
	}

	public String issueAccessToken(UUID userId, String email, Set<Role> roles) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(accessTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("email", email)
				.claim("roles", roles.stream().map(Role::getName).collect(Collectors.toSet()))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String issuePasswordResetToken(UUID userId, String email, long passwordResetTtlSeconds) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(passwordResetTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("email", email).claim("typ", "password-reset")
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String issueActivationToken(UUID userId, String email, long activationTtlSeconds) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(activationTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("email", email).claim("typ", "activation")
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

}
