package com.example.orderservice.dto;

public class StockReserveResponse {

	private boolean reserved;

	public StockReserveResponse() {
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

}
