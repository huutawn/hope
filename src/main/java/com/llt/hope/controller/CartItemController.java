package com.llt.hope.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.CartItemResponse;
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
}
