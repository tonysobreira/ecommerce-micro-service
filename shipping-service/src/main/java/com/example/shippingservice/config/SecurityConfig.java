package com.example.shippingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.shippingservice.security.JwtAuthFilter;
import com.example.shippingservice.security.JwtVerifier;

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
						.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers(HttpMethod.POST, "/shipping/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(verifier), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
