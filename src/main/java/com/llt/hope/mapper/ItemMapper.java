package com.llt.hope.mapper;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    OrderItem toItem(OrderItemCreationRequest request);

    OrderItemResponse toItemResponse(OrderItem orderItem);

}
