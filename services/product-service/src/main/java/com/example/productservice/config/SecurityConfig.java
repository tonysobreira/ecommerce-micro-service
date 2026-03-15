package com.example.productservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.productservice.infra.CorrelationIdFilter;
import com.example.productservice.security.JwtAuthFilter;
import com.example.productservice.security.JwtVerifier;

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
						// Public GETs
						.requestMatchers(HttpMethod.GET, "/products/**", "/categories/**", "/product-images/**")
						.permitAll()

						// Internal product endpoints (service-to-service only)
						.requestMatchers(HttpMethod.GET, "/internal/products/quote").authenticated()
						.requestMatchers(HttpMethod.POST, "/internal/products/stock/reserve").authenticated()
						.requestMatchers(HttpMethod.POST, "/internal/products/stock/release").authenticated()

						// Swagger/OpenAPI
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

						.requestMatchers("/actuator/health").permitAll()

						// ADMIN writes
						.requestMatchers(HttpMethod.POST, "/products/**", "/categories/**", "/product-images/**")
						.hasRole("ADMIN").requestMatchers(HttpMethod.PUT, "/products/**", "/categories/**")
						.hasRole("ADMIN").requestMatchers(HttpMethod.PATCH, "/products/**", "/categories/**")
						.hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/products/**", "/categories/**", "/product-images/**")
						.hasRole("ADMIN")

						.anyRequest().authenticated())
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
