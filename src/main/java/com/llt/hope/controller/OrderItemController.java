package com.llt.hope.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.service.OrderItemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/orderItem")
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
}
