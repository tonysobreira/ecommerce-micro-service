package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StockReleaseRequest(
	@NotEmpty
	List<StockReserveItem> items
) {

	public List<StockReserveItem> getItems() {
		return items;
	}

}
