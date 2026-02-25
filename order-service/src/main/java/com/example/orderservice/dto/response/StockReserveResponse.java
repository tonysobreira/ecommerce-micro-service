package com.example.orderservice.dto.response;

public record StockReserveResponse(
	boolean reserved
) {

	public boolean getReserved() {
		return reserved;
	}

	public boolean isReserved() {
		return reserved;
	}

}
