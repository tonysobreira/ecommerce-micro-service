package com.example.cartservice.dto.request;

import java.util.List;

public record CreateOrderRequest(
	List<CreateOrderItemRequest> items,
	AddressRequest shippingAddress,
	String paymentMethod
) {

}
