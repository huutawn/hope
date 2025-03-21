package com.llt.hope.service;


import com.llt.hope.dto.request.CartItemCreationRequest;
import com.llt.hope.dto.request.CartItemUpdateRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.CartItemResponse;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.OrderItem;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("isAuthenticated()")
public class CartItemService {
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    CartItemMapper cartItemMapper;

    public CartItemResponse addCartItem(CartItemCreationRequest request, Authentication authentication){
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_));

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
    public PageResponse<CartItemResponse> getAllCartItem(Specification<CartItem> spec, int page, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CartItem> items = cartItemRepository.findAll(pageable);
        List<CartItemResponse> cartItemResponses =
                items.getContent().stream().map(cartItemMapper::toCartItemResponse).toList();
        return PageResponse.<CartItemResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(items.getTotalElements())
                .totalPages(items.getTotalPages())
                .data(cartItemResponses)
                .build();
    }
    public CartItemResponse getCart(Long id) {
        return cartItemMapper.toCartItemResponse(
                cartItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }
    public void deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new AppException(ErrorCode.CARTITEM_NOT_EXISTED);
        }
        productRepository.deleteById(cartItemId);
    }
    public CartItemResponse updateCart(Long id, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CARTITEM_NOT_EXISTED));
        cartItemMapper.updateCart(cartItem, request);

        return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
    }
}
