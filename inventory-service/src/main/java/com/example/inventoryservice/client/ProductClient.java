package com.example.inventoryservice.client;

import com.example.inventoryservice.config.FeignConfig;
import com.example.inventoryservice.dto.response.ProductQuoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${product-service.name:product-service}", configuration = FeignConfig.class)
public interface ProductClient {

	@GetMapping("/internal/products/quote")
	ProductQuoteResponse quote(@RequestParam("ids") String idsCsv);

}
