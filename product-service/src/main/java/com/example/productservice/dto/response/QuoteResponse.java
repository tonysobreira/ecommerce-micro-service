package com.example.productservice.dto.response;

import java.util.List;

public record QuoteResponse(
	List<QuoteItemResponse> items
) {

	public List<QuoteItemResponse> getItems() {
		return items;
	}

}
