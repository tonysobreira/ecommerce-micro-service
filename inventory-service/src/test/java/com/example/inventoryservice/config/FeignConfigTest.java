package com.example.inventoryservice.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

class FeignConfigTest {

	@AfterEach
	void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	void correlationAndAuthorizationHeadersShouldBeForwarded() {
		FeignConfig feignConfig = new FeignConfig();
		RequestInterceptor interceptor = feignConfig.headersForwardingInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-Correlation-Id", "cid-123");
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		RequestTemplate template = new RequestTemplate();

		interceptor.apply(template);

		assertEquals("cid-123", template.headers().get("X-Correlation-Id").iterator().next());
		assertEquals("Bearer token", template.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());
	}

	@Test
	void shouldNotFailWhenNoServletContextExists() {
		FeignConfig feignConfig = new FeignConfig();
		RequestInterceptor interceptor = feignConfig.headersForwardingInterceptor();
		RequestTemplate template = new RequestTemplate();

		interceptor.apply(template);

		assertTrue(template.headers().isEmpty());
	}

}
