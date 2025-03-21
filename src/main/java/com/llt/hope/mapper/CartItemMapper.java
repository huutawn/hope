package com.llt.hope.mapper;


import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.dto.request.CartItemUpdateRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.CartItemResponse;
import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem toCartItem(CartItemCreationRequest request);

    CartItemResponse toCartItemResponse(CartItem cartItem);

    void updateCart(@MappingTarget CartItem cartItem, CartItemUpdateRequest request);

}
