package com.example.cartservice.mapper;

import org.mapstruct.Mapper;

import com.example.cartservice.dto.response.CartItemResponse;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.model.CartDocument;
import com.example.cartservice.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

	CartResponse toResponse(CartDocument cart);

	CartItemResponse toResponse(CartItem item);

}