package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StockReserveRequest(
	@NotEmpty
	List<StockReserveItem> items
) {

}
