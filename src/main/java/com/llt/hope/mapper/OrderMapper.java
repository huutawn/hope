package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.request.OrderUpdateRequest;
import com.llt.hope.dto.response.OrderResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.User;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paymentStatus", constant = "PENDING")
    Order toOrder(OrderCreationRequest request, User buyer);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderUpdateRequest request);
}
