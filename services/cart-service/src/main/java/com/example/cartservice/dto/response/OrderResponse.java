package com.example.cartservice.dto.response;

import java.util.UUID;

public record OrderResponse(
	UUID id,

	String status
) {

}
