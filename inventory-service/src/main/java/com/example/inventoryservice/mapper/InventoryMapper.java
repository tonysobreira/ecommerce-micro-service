package com.example.inventoryservice.mapper;

import org.mapstruct.Mapper;

import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.model.Inventory;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

	InventoryResponse toResponse(Inventory inventory);

}
