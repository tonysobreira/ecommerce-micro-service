package com.example.paymentservice.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor headersForwardingInterceptor() {
		return template -> {
			// Forward correlation id from MDC (set by servlet filter)
			String cid = MDC.get("correlationId");

			if (cid != null && !cid.isBlank()) {
				template.header("X-Correlation-Id", cid);
			}

			// Forward Authorization if present in current request (stored by servlet
			// container in a ThreadLocal is not safe to assume)
			// Instead of trying to read HttpServletRequest here, we rely on the fact that
			// internal endpoints can also accept
			// gateway/service auth. If you want strict forward, we can implement a custom
			// interceptor reading RequestContextHolder.
			// For now, we keep only correlation-id.
		};
	}

	@Bean
	public RequestInterceptor authHeaderForwardInterceptor() {
		return template -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attrs == null) {
				return;
			}

			HttpServletRequest request = attrs.getRequest();
			String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (auth != null && !auth.isBlank()) {
				template.header(HttpHeaders.AUTHORIZATION, auth);
			}
		};
	}

}