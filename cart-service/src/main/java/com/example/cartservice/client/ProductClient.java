package com.example.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cartservice.config.FeignConfig;
import com.example.cartservice.dto.response.ProductQuoteResponse;

@FeignClient(name = "${product-service.name:product-service}", path = "/internal", configuration = FeignConfig.class)
public interface ProductClient {

	@GetMapping("/products/quote")
	ProductQuoteResponse quote(@RequestParam("ids") String ids);

}
