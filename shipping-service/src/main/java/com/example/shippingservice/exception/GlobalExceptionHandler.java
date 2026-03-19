package com.example.shippingservice.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		log.error("Validation exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Validation error");
		pd.setType(URI.create("https://example.com/problems/validation"));

		Map<String, String> errors = new HashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errors.put(fe.getField(), fe.getDefaultMessage());
		}

		pd.setProperty("errors", errors);
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAuthentication(AuthenticationException ex) {
		log.error("Unauthorized exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Authentication");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/authentication"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ProblemDetail handleUnauthorized(UnauthorizedException ex) {
		log.error("Unauthorized exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		pd.setTitle("Unauthorized");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/unauthorized"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
		log.error("Access Denied exception", ex);

		ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
		pd.setTitle("Access Denied");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://api.ecommerce.com/errors/access-denied"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(BadRequestException.class)
	public ProblemDetail handleBadRequest(BadRequestException ex) {
		log.error("Bad Request exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Bad Request");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/bad-request"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(NotFoundException.class)
	public ProblemDetail handleNotFound(NotFoundException ex) {
		log.error("Not Found exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		pd.setTitle("Not Found");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/not-found"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ForbiddenException.class)
	public ProblemDetail handleForbidden(ForbiddenException ex) {
		log.error("Forbidden exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
		pd.setTitle("Forbidden");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/forbidden"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ConflictException.class)
	public ProblemDetail handleConflict(ConflictException ex) {
		log.error("Conflict exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		pd.setTitle("Conflict");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/conflict"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(IllegalStateException.class)
	public ProblemDetail handleIllegalState(IllegalStateException ex) {
		log.error("Invalid State exception", ex);

		ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		pd.setTitle("Invalid State");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://api.ecommerce.com/errors/invalid-state"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
		log.error("Constraint Violation exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		pd.setTitle("Constraint Violation");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/constraint-violation"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		log.error("Data Integrity Violation Exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		pd.setTitle("Data Integrity Violation");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/data-integrity-violation"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGeneric(Exception ex) {
		log.error("Unhandled exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		pd.setTitle("Internal Error");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/internal"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ShipmentNotFoundException.class)
	public ProblemDetail handleShipmentNotFound(ShipmentNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setType(URI.create("https://api.ecommerce.com/errors/shipment-not-found"));
		problem.setTitle("Shipment Not Found");
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}

	@ExceptionHandler(ShippingMethodNotFoundException.class)
	public ProblemDetail handleShippingMethodNotFound(ShippingMethodNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setType(URI.create("https://api.ecommerce.com/errors/shipping-method-not-found"));
		problem.setTitle("Shipping Method Not Found");
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}

	@ExceptionHandler(TrackingNotFoundException.class)
	public ProblemDetail handleTrackingNotFound(TrackingNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setType(URI.create("https://api.ecommerce.com/errors/trackin-not-found"));
		problem.setTitle("Trackin Not Found");
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}

}
