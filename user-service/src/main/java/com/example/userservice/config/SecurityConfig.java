package com.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userservice.infra.CorrelationIdFilter;
import com.example.userservice.security.JwtAuthFilter;
import com.example.userservice.security.JwtVerifier;

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
				.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers(HttpMethod.POST, "/internal/users/profiles").permitAll()
						.requestMatchers("/users").hasRole("ADMIN").anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(verifier), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
		FilterRegistrationBean<CorrelationIdFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorrelationIdFilter());
		bean.setOrder(-200);
		return bean;
	}

}
