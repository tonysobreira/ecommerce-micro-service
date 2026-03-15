package com.example.paymentservice.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StockReleaseRequest(
	@NotNull UUID orderId,
	@NotEmpty List<StockReserveItem> items
) {

}
