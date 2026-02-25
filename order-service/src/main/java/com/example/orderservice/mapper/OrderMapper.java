package com.example.orderservice.mapper;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatusHistory;
import com.example.orderservice.dto.response.OrderItemResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.OrderStatusHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	@Mapping(target = "shippingAddress.line1", source = "order.shipLine1")
	@Mapping(target = "shippingAddress.line2", source = "order.shipLine2")
	@Mapping(target = "shippingAddress.city", source = "order.shipCity")
	@Mapping(target = "shippingAddress.state", source = "order.shipState")
	@Mapping(target = "shippingAddress.zip", source = "order.shipZip")
	@Mapping(target = "shippingAddress.country", source = "order.shipCountry")
	@Mapping(target = "items", source = "items")
	@Mapping(target = "statusHistory", source = "history")
	OrderResponse toResponse(Order order, List<OrderItem> items, List<OrderStatusHistory> history);

	OrderItemResponse toOrderItemResponse(OrderItem item);

	OrderStatusHistoryResponse toOrderStatusHistoryResponse(OrderStatusHistory history);

}
