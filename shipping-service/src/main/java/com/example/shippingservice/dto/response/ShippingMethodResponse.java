package com.example.shippingservice.dto.response;

import java.math.BigDecimal;

public record ShippingMethodResponse(
	String name,

	BigDecimal baseCost
) {

}
