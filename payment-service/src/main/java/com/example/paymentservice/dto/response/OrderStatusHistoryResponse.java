package com.example.paymentservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusHistoryResponse(
	String status,

	UUID changedBy,

	Instant changedAt
) {

}
