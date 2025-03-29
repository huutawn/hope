package com.llt.hope.mapper;

import org.mapstruct.Mapping;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {

    @Mapping(source = "order.id", target = "orderId") // ✅ Ánh xạ orderId đúng
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

}
