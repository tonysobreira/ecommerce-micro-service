package com.example.cartservice.dto.response;

import java.util.List;

public record ProductQuoteResponse(
	List<ProductQuoteItemResponse> items
) {

}
