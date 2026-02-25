package com.example.orderservice.mapper;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatusHistory;
import com.example.orderservice.dto.request.AddressDto;
import com.example.orderservice.dto.response.OrderItemResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.OrderStatusHistoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

	public OrderResponse toResponse(Order order, List<OrderItem> items, List<OrderStatusHistory> history) {
		AddressDto addr = new AddressDto();
		addr.setLine1(order.getShipLine1());
		addr.setLine2(order.getShipLine2());
		addr.setCity(order.getShipCity());
		addr.setState(order.getShipState());
		addr.setZip(order.getShipZip());
		addr.setCountry(order.getShipCountry());

		List<OrderItemResponse> itemDtos = items.stream()
				.map(i -> new OrderItemResponse(i.getProductId(), i.getQuantity(), i.getUnitPriceCents(), i.getCurrency()))
				.toList();

		List<OrderStatusHistoryResponse> histDtos = history.stream()
				.map(h -> new OrderStatusHistoryResponse(h.getStatus(), h.getChangedBy(), h.getChangedAt())).toList();

		return new OrderResponse(order.getId(), order.getUserId(), order.getStatus(), order.getPaymentMethod(), addr,
				order.getCurrency(), order.getSubtotalCents(), order.getShippingCents(), order.getTotalCents(),
				order.getCreatedAt(), order.getUpdatedAt(), itemDtos, histDtos);
	}
}
