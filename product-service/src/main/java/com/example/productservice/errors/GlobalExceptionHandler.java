package com.example.productservice.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Validation error");
		pd.setType(URI.create("https://example.com/problems/validation"));

		Map<String, String> errors = new HashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errors.put(fe.getField(), fe.getDefaultMessage());
		}
		pd.setProperty("errors", errors);
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(NotFoundException.class)
	public ProblemDetail handleNotFound(NotFoundException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		pd.setTitle("Not found");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/not-found"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(BadRequestException.class)
	public ProblemDetail handleBadRequest(BadRequestException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle("Bad request");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/bad-request"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ConflictException.class)
	public ProblemDetail handleConflict(ConflictException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		pd.setTitle("Conflict");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/conflict"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(ForbiddenException.class)
	public ProblemDetail handleForbidden(ForbiddenException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
		pd.setTitle("Forbidden");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/forbidden"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ProblemDetail handleUnauthorized(UnauthorizedException ex) {
		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		pd.setTitle("Unauthorized");
		pd.setDetail(ex.getMessage());
		pd.setType(URI.create("https://example.com/problems/unauthorized"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGeneric(Exception ex) {
		log.error("Unhandled exception", ex);

		ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		pd.setTitle("Internal error");
		pd.setDetail("Unexpected error");
		pd.setType(URI.create("https://example.com/problems/internal"));
		pd.setProperty("correlationId", MDC.get("correlationId"));
		return pd;
	}

}
