package com.llt.hope.service;


import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.dto.response.CartItemResponse;
import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CartItemMapper;
import com.llt.hope.repository.jpa.CartItemRepository;
import com.llt.hope.repository.jpa.ProductRepository;
import com.llt.hope.repository.jpa.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartItemService {
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    CartItemMapper cartItemMapper;

    public CartItemResponse createCartItem(CartItemCreationRequest request, Authentication authentication){
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(currentUser, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .user(currentUser)
                    .product(product)
                    .quantity(request.getProductId())
                    .build();
        }
        BigDecimal quantityBD = BigDecimal.valueOf(cartItem.getQuantity());
        BigDecimal totalPrice = cartItem.getProduct().getPrice().multiply(quantityBD);
        cartItem = cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .id(cartItem.getId())
                .userId(cartItem.getUser())
                .productId(cartItem.getProduct())
                .addedAt(LocalDateTime.now())
                .totalPrice(totalPrice)
                .quantity(cartItem.getQuantity())
                .build();
        return cartItemResponse;
    }
}
