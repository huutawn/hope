package com.llt.hope.controller;

import jakarta.validation.Valid;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.request.OrderUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.OrderResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.service.OrderService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderCreationRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    @GetMapping("/getAllOrder")
    public ApiResponse<PageResponse<OrderResponse>> getAllOrders(
            @Filter Specification<Order> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.getAllOrder(spec, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrders(Long id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(id))
                .build();
    }

    @PatchMapping("/{id}") // SAU KHI THANH TOÁN THÀNH CÔNG NÕ
    public ApiResponse<OrderResponse> updateOrder(
            @PathVariable Long id, @RequestBody @Valid OrderUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, request);

        return ApiResponse.<OrderResponse>builder().result(updatedOrder).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ApiResponse.<String>builder()
                .result("Order deleted successfully")
                .build();
    }
}
