package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    OrderItem toItem(OrderItemCreationRequest request);

    OrderItemResponse toItemResponse(OrderItem orderItem);

}
