package com.example.shippingservice.exception;

public class TrackingNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TrackingNotFoundException(String message) {
		super(message);
	}

}