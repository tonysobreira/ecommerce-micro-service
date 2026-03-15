package com.example.productservice.dto.response;

import java.util.List;

public record QuoteResponse(
	List<QuoteItemResponse> items
) {

}
