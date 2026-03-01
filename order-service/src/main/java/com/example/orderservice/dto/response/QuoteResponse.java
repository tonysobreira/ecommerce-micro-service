package com.example.orderservice.dto.response;

import java.util.List;

public record QuoteResponse(
	List<QuoteItemResponse> items
) {

}
