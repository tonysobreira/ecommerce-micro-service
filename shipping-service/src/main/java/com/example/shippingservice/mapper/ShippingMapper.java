package com.example.shippingservice.mapper;

import org.mapstruct.Mapper;

import com.example.shippingservice.dto.response.ShipmentResponse;
import com.example.shippingservice.dto.response.ShippingMethodResponse;
import com.example.shippingservice.dto.response.TrackingResponse;
import com.example.shippingservice.model.Shipment;
import com.example.shippingservice.model.ShippingMethod;
import com.example.shippingservice.model.Tracking;

@Mapper(componentModel = "spring")
public interface ShippingMapper {

	ShipmentResponse toResponse(Shipment shipment);

	ShippingMethodResponse toResponse(ShippingMethod shipment);

	TrackingResponse toResponse(Tracking tracking);

}
