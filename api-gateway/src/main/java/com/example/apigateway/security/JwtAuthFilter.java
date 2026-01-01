package com.example.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

	private final JwtVerifier verifier;

	public JwtAuthFilter(@Value("${security.jwt.secret}") String secret) {
		this.verifier = new JwtVerifier(secret);
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "";

		if (isPublic(path, method)) {
			return chain.filter(exchange);
		}

		String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (auth == null || !auth.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		try {
			Claims claims = verifier.verify(auth.substring(7)).getBody();

			String userId = claims.getSubject();
			String email = claims.get("email", String.class);
			@SuppressWarnings("unchecked")
			List<String> roles = claims.get("roles", List.class);

			ServerHttpRequest mutated = exchange.getRequest().mutate().header("X-User-Id", userId)
					.header("X-User-Email", email == null ? "" : email)
					.header("X-User-Roles", roles == null ? "" : String.join(",", roles)).build();

			return chain.filter(exchange.mutate().request(mutated).build());
		} catch (JwtException e) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}

	private boolean isPublic(String path, String method) {
		if (path.startsWith("/auth")) {
			return true;
		}

		if (("GET".equalsIgnoreCase(method)) && (path.startsWith("/products") || path.startsWith("/categories")
				|| path.startsWith("/product-images"))) {
			return true;
		}

		return path.startsWith("/actuator/health");
	}

	@Override
	public int getOrder() {
		return -1;
	}

}
