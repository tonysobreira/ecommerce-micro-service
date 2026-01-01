package com.example.productservice.config;

import com.example.productservice.security.JwtAuthFilter;
import com.example.productservice.security.JwtVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public JwtVerifier jwtVerifier(@Value("${security.jwt.secret}") String secret) {
		return new JwtVerifier(secret);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtVerifier verifier) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// public GETs
						.requestMatchers(org.springframework.http.HttpMethod.GET, "/products/**", "/categories/**",
								"/product-images/**")
						.permitAll().requestMatchers("/actuator/health").permitAll()

						// internal endpoints require auth
//						.requestMatchers("/internal/**").authenticated()
						.requestMatchers("/internal/**").permitAll()

						// product/category/image writes require ADMIN
						.requestMatchers(org.springframework.http.HttpMethod.POST, "/products/**", "/categories/**",
								"/product-images/**")
						.hasRole("ADMIN")
						.requestMatchers(org.springframework.http.HttpMethod.PUT, "/products/**", "/categories/**")
						.hasRole("ADMIN")
						.requestMatchers(org.springframework.http.HttpMethod.PATCH, "/products/**", "/categories/**")
						.hasRole("ADMIN")
						.requestMatchers(org.springframework.http.HttpMethod.DELETE, "/products/**", "/categories/**",
								"/product-images/**")
						.hasRole("ADMIN")

						.anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(verifier),
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.productservice.infra.CorrelationIdFilter> correlationIdFilter() {
		org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.productservice.infra.CorrelationIdFilter> bean = new org.springframework.boot.web.servlet.FilterRegistrationBean<>();
		bean.setFilter(new com.example.productservice.infra.CorrelationIdFilter());
		bean.setOrder(-200);
		return bean;
	}

}
