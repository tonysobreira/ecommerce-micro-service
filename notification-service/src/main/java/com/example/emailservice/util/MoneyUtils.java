package com.example.emailservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

	private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

	private MoneyUtils() {
	}

	public static BigDecimal centsToAmount(BigDecimal cents) {
		return cents.divide(ONE_HUNDRED, 2, RoundingMode.UNNECESSARY);
	}

}
