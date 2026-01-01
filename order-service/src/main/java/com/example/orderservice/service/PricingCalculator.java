package com.example.orderservice.service;

public final class PricingCalculator {

	private PricingCalculator() {
	}

	// keep it simple for now (flat shipping)
	public static long shippingCents(long subtotalCents) {
		return subtotalCents >= 5000 ? 0 : 999; // free over 50.00
	}

}
