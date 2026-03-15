package com.example.shippingservice.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMethodRequest(@NotBlank String name, @NotNull BigDecimal baseCost) {
}
