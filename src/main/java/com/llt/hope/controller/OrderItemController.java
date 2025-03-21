package com.llt.hope.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.OrderItem;
import com.llt.hope.service.OrderItemService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/orderItem")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
public class OrderItemController {
    OrderItemService orderItemService;

    @PostMapping
    public ApiResponse<OrderItemResponse> createItem(@RequestBody OrderItemCreationRequest request) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.createItem(request))
                .build();
    }

    @GetMapping("/getAllOrderItems")
    public ApiResponse<PageResponse<OrderItemResponse>> getAllOrderItem(
            @Filter Specification<OrderItem> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<OrderItemResponse>>builder()
                .result(orderItemService.getAllOrderItem(spec, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderItemResponse> getOrderItems(Long id) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.getOrderItem(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteOrderItems(@PathVariable Long id) {
        orderItemService.deleteOrderItems(id);
        return ApiResponse.<String>builder()
                .result("Order deleted successfully")
                .build();
    }
}
