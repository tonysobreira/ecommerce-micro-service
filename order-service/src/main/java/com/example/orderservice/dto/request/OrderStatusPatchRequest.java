package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public class OrderStatusPatchRequest {

	@NotBlank
	private String status;

	public OrderStatusPatchRequest() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
