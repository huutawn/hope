package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.dto.response.CartItemResponse;
import com.llt.hope.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem toCartItem(CartItemCreationRequest request);

    CartItemResponse toCartItemResponse(CartItem cartItem);
}
