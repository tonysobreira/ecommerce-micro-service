package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusPatchRequest(
	@NotBlank
	String status
) {

}
