package com.example.orderservice.dto;

import java.util.List;

public class QuoteResponse {

	private List<QuoteItemResponse> items;

	public QuoteResponse() {
	}

	public List<QuoteItemResponse> getItems() {
		return items;
	}

	public void setItems(List<QuoteItemResponse> items) {
		this.items = items;
	}

}
