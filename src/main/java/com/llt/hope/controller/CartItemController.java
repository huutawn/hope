package com.llt.hope.controller;

import com.llt.hope.dto.request.CartItemUpdateRequest;
import com.llt.hope.dto.request.UserUpdateRequest;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Order;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.service.CartItemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cartItem")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartItemController {
    CartItemService cartItemService;

    @PostMapping
    public ApiResponse<CartItemResponse> createCartItem(
            @Valid @RequestBody CartItemCreationRequest request, Authentication authentication) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartItemService.addCartItem(request, authentication))
                .build();
    }
    @PatchMapping("/{id}")
    ApiResponse<CartItemResponse> updateCart(@PathVariable Long id, @RequestBody @Valid CartItemUpdateRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartItemService.updateCart(id, request))
                .build();
    }
    @GetMapping("/getAllcart")
    public ApiResponse<PageResponse<CartItemResponse>> getAllCartItem(
            @Filter Specification<CartItem> spec,

            @RequestParam(value = "page", required = false, defaultValue = "1") int page,

            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        return ApiResponse.<PageResponse<CartItemResponse>>builder()
                .result(cartItemService.getAllCartItem(spec,page,size))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<CartItemResponse> getCartItems(Long id){
        return ApiResponse.<CartItemResponse>builder()
                .result(cartItemService.getCart(id))
                .build();}
}
