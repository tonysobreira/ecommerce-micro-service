package com.example.shippingservice.exception;

public class ShippingMethodNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ShippingMethodNotFoundException(String message) {
		super(message);
	}

}