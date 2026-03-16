package com.example.shippingservice.exception;

public class ShipmentNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ShipmentNotFoundException(String message) {
		super(message);
	}

}