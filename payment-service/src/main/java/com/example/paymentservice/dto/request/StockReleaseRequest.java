package com.example.paymentservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StockReleaseRequest(
	@NotEmpty
	List<StockReserveItem> items
) {

}
