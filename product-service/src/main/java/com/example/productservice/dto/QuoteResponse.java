package com.example.productservice.dto;

import java.util.List;

public class QuoteResponse {

	private List<QuoteItemResponse> items;

	public QuoteResponse() {
	}

	public QuoteResponse(List<QuoteItemResponse> items) {
		this.items = items;
	}

	public List<QuoteItemResponse> getItems() {
		return items;
	}

}
