package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class StockReleaseRequest {

	@NotEmpty
	private List<StockReserveItem> items;

	public StockReleaseRequest() {
	}

	public StockReleaseRequest(List<StockReserveItem> items) {
		this.items = items;
	}

	public List<StockReserveItem> getItems() {
		return items;
	}

	public void setItems(List<StockReserveItem> items) {
		this.items = items;
	}

}
