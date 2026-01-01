package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class StockReserveRequest {

	@NotEmpty
	private List<StockReserveItem> items;

	public StockReserveRequest() {
	}

	public StockReserveRequest(List<StockReserveItem> items) {
		this.items = items;
	}

	public List<StockReserveItem> getItems() {
		return items;
	}

	public void setItems(List<StockReserveItem> items) {
		this.items = items;
	}

}
