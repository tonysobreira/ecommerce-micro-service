package com.example.inventoryservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor correlationForwarder() {
		return template -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs == null) return;
			HttpServletRequest req = attrs.getRequest();
			String cid = req.getHeader("X-Correlation-Id");
			if (cid != null && !cid.isBlank()) {
				template.header("X-Correlation-Id", cid);
			}
		};
	}
}
