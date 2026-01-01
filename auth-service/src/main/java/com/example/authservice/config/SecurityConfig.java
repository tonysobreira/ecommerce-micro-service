package com.example.authservice.config;

import com.example.authservice.security.JwtAuthFilter;
import com.example.authservice.security.JwtIssuer;
import com.example.authservice.security.JwtVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtVerifier jwtVerifier(@Value("${security.jwt.secret}") String secret) {
		return new JwtVerifier(secret);
	}

	@Bean
	public JwtIssuer jwtIssuer(@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.issuer}") String issuer,
			@Value("${security.jwt.access-ttl-minutes}") long accessTtlMinutes) {
		return new JwtIssuer(secret, issuer, accessTtlMinutes * 60);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtVerifier verifier) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register", "/auth/login", "/actuator/health")
						.permitAll().anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(verifier),
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.authservice.infra.CorrelationIdFilter> correlationIdFilter() {
		org.springframework.boot.web.servlet.FilterRegistrationBean<com.example.authservice.infra.CorrelationIdFilter> bean = new org.springframework.boot.web.servlet.FilterRegistrationBean<>();
		bean.setFilter(new com.example.authservice.infra.CorrelationIdFilter());
		bean.setOrder(-200);
		return bean;
	}

}
