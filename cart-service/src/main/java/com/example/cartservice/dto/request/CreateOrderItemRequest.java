package com.example.cartservice.dto.request;

import java.util.UUID;

public record CreateOrderItemRequest(
	UUID productId,
	Integer quantity
) {

}
