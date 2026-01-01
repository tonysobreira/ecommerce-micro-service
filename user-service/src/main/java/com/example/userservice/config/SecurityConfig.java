package com.example.userservice.config;

import com.example.userservice.security.JwtAuthFilter;
import com.example.userservice.security.JwtVerifier;
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
				.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health").permitAll()
						.requestMatchers("/users").hasRole("ADMIN").anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(verifier),
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.userservice.infra.CorrelationIdFilter> correlationIdFilter() {
		org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.userservice.infra.CorrelationIdFilter> bean = new org.springframework.boot.web.servlet.FilterRegistrationBean<>();
		bean.setFilter(new com.example.userservice.infra.CorrelationIdFilter());
		bean.setOrder(-200);
		return bean;
	}

}
