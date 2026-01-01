package com.example.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdGatewayFilter implements GlobalFilter, Ordered {

	public static final String HEADER = "X-Correlation-Id";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String cid = exchange.getRequest().getHeaders().getFirst(HEADER);
		if (cid == null || cid.isBlank()) {
			cid = UUID.randomUUID().toString();
		}

		ServerHttpRequest request = exchange.getRequest().mutate().header(HEADER, cid).build();

		return chain.filter(exchange.mutate().request(request).build());
	}

	@Override
	public int getOrder() {
		return -2;
	}

}
