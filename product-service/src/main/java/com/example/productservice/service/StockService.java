package com.example.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productservice.dto.request.StockReserveItem;
import com.example.productservice.exception.BadRequestException;

@Service
public class StockService {

	public void reserve(List<StockReserveItem> items) {
		throw new BadRequestException("Stock operations moved to inventory-service");
	}

	public void release(List<StockReserveItem> items) {
		throw new BadRequestException("Stock operations moved to inventory-service");
	}
}
