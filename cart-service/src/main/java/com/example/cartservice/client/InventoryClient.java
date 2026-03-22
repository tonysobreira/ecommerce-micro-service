package com.example.cartservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cartservice.config.FeignConfig;
import com.example.cartservice.dto.response.InventoryAvailabilityResponse;

@FeignClient(name = "${inventory-service.name:inventory-service}", path = "/internal", configuration = FeignConfig.class)
public interface InventoryClient {

	@GetMapping("/inventory/availability")
	List<InventoryAvailabilityResponse> availability(@RequestParam("ids") String idsCsv);

}
