// package com.llt.hope.mapper;
//
//
// import com.llt.hope.dto.request.OrderCreationRequest;
// import com.llt.hope.dto.request.OrderUpdateRequest;
// import com.llt.hope.dto.response.OrderResponse;
// import com.llt.hope.entity.Order;
// import com.llt.hope.service.OrderService;
// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;
// import org.mapstruct.MappingTarget;
// import org.mapstruct.factory.Mappers;
//
// @Mapper(componentModel = "spring")
// public interface OrderMapper {
//        OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
//
//        @Mapping(target = "orderId", ignore = true)
//        @Mapping(target = "buyer", ignore = true) // Will be set in the service layer
//        @Mapping(target = "orderDate", ignore = true)
//        @Mapping(target = "shippingFee", ignore = true)
//        @Mapping(target = "taxAmount", ignore = true)
//        @Mapping(target = "grandTotal", ignore = true)
//        @Mapping(target = "status", constant = "PENDING")
//        @Mapping(target = "paymentStatus", constant = "PENDING")
//        OrderService toOrder(OrderCreationRequest orderCreationRequest);
//
//        @Mapping(source = "buyer.name", target = "buyer")
//        OrderResponse toOrderResponse(OrderService order);
//
//        @Mapping(target = "orderId", ignore = true)
//        @Mapping(target = "buyer", ignore = true)
//        @Mapping(target = "orderDate", ignore = true)
//        @Mapping(target = "totalAmount", ignore = true)
//        @Mapping(target = "shippingFee", ignore = true)
//        @Mapping(target = "taxAmount", ignore = true)
//        @Mapping(target = "grandTotal", ignore = true)
//        @Mapping(target = "shippingAddress", ignore = true)
//        @Mapping(target = "shippingCity", ignore = true)
//        @Mapping(target = "shippingState", ignore = true)
//        @Mapping(target = "shippingPostalCode", ignore = true)
//        @Mapping(target = "shippingCountry", ignore = true)
//        @Mapping(target = "paymentMethod", ignore = true)
//        @Mapping(target = "paymentStatus", ignore = true)
//        @Mapping(target = "notes", ignore = true)
//        void updateOrderFromDto(@MappingTarget Order order, OrderUpdateRequest orderUpdateRequest);
//    }
