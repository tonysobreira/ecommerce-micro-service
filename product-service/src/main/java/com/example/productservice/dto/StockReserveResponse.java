package com.example.productservice.dto;

public class StockReserveResponse {

	private boolean reserved;

	public StockReserveResponse() {
	}

	public StockReserveResponse(boolean reserved) {
		this.reserved = reserved;
	}

	public boolean isReserved() {
		return reserved;
	}

}
