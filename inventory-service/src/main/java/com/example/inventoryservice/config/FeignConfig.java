package com.example.inventoryservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor headersForwardingInterceptor() {
		return template -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attrs == null) {
				return;
			}

			HttpServletRequest request = attrs.getRequest();
			String cid = request.getHeader("X-Correlation-Id");
			String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (cid != null && !cid.isBlank()) {
				template.header("X-Correlation-Id", cid);
			}

			if (auth != null && !auth.isBlank()) {
				template.header(HttpHeaders.AUTHORIZATION, auth);
			}
		};
	}

}
