package com.example.orderservice.service;

import java.math.BigDecimal;

public final class PricingCalculator {

	private PricingCalculator() {
	}

	// keep it simple for now (flat shipping)
	// free over 50.00
	public static BigDecimal shippingCents(BigDecimal subtotalCents) {
		return subtotalCents.compareTo(BigDecimal.valueOf(5000)) >= 0 ? BigDecimal.ZERO : BigDecimal.valueOf(999);
	}

}
