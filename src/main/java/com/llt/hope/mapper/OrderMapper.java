package com.llt.hope.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.request.OrderUpdateRequest;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.dto.response.OrderResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.OrderItem;
import com.llt.hope.entity.User;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paymentStatus", constant = "PENDING")
    Order toOrder(OrderCreationRequest request, User buyer);

    @Mapping(source = "id", target = "orderId") // 🔥 Đảm bảo orderId không bị null
    @Mapping(source = "buyer.id", target = "buyerId") // 🔥 Lấy ID của buyer
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderUpdateRequest request);

    default List<OrderItemResponse> mapOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null) return new ArrayList<>();
        return orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getItemId(),
                        item.getOrder().getId(), // Trả về orderId
                        item.getProduct().getId(), // Trả về productId
                        item.getProduct().getName(), // Trả về productName
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal()))
                .toList();
    }
}
