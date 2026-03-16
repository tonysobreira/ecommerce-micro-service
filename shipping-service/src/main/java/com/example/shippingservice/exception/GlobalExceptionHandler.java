package com.example.shippingservice.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

	@ExceptionHandler(IllegalStateException.class)
	public ProblemDetail handleIllegalState(IllegalStateException ex) {
		ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		pd.setTitle("Invalid State");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://api.ecommerce.com/errors/invalid-state"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Validation error");
		pd.setType(URI.create("https://example.com/problems/validation"));

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			errors.put(fieldName, error.getDefaultMessage());
		});

		pd.setProperty("errors", errors);
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(BadRequestException.class)
	public ProblemDetail handleBadRequest(BadRequestException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Bad request");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/bad-request"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(NotFoundException.class)
	public ProblemDetail handleNotFound(NotFoundException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		pd.setTitle("Not found");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/not-found"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
		pd.setTitle("Forbidden");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/forbidden"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGenericException(Exception ex) {
		log.error("Unhandled exception: ", ex);

		ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred");
		pd.setTitle("Internal Server Error");
		pd.setDetail("Unexpected error");
		pd.setType(URI.create("https://example.com/problems/internal"));
		pd.setProperty("timestamp", Instant.now());
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

}
