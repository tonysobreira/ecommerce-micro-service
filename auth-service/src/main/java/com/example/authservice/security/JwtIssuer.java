package com.example.authservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtIssuer {

	private final SecretKey key;

	private final String issuer;

	private final long accessTtlSeconds;

	private final long refreshTtlSeconds;

	public JwtIssuer(String secret, String issuer, long accessTtlSeconds, long refreshTtlSeconds) {
		if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
			throw new IllegalStateException("JWT_SECRET must be at least 32 bytes for HS256");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.issuer = issuer;
		this.accessTtlSeconds = accessTtlSeconds;
		this.refreshTtlSeconds = refreshTtlSeconds;
	}

	public String issueAccessToken(UUID userId, String email, List<String> roles) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(accessTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("email", email).claim("roles", roles)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String issueActivationToken(UUID userId, String email, long activationTtlSeconds) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(activationTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("email", email).claim("typ", "activation")
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String issueRefreshToken(UUID userId) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(refreshTtlSeconds);

		return Jwts.builder().setIssuer(issuer).setSubject(userId.toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp)).claim("typ", "refresh").signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

}
