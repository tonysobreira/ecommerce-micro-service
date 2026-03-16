package com.example.paymentservice.mapper;

import org.mapstruct.Mapper;

import com.example.paymentservice.dto.response.PaymentResponse;
import com.example.paymentservice.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

	PaymentResponse toResponse(Payment payment);

}