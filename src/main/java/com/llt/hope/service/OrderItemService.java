package com.llt.hope.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.dto.response.OrderItemResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ItemMapper;
import com.llt.hope.repository.jpa.OrderItemRepository;
import com.llt.hope.repository.jpa.OrderRepository;
import com.llt.hope.repository.jpa.ProductRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderItemService {
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    ItemMapper orderItemMapper;
    UserRepository userRepository;

    public OrderItemResponse createItem(OrderItemCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .subtotal(request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();
        orderItem = orderItemRepository.save(orderItem);
        OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                .itemId(orderItem.getItemId())
                .productId(product)
                .price(orderItem.getPrice())
                .orderId(order)
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getSubtotal())
                .build();
        //        updateOrderTotal(order);

        return orderItemResponse;
    }

    public PageResponse<OrderItemResponse> getAllOrderItem(Specification<OrderItem> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<OrderItem> items = orderItemRepository.findAll(pageable);
        List<OrderItemResponse> orderItemResponses =
                items.getContent().stream().map(orderItemMapper::toItemResponse).toList();
        return PageResponse.<OrderItemResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(items.getTotalElements())
                .totalPages(items.getTotalPages())
                .data(orderItemResponses)
                .build();
    }

    public OrderItemResponse getOrderItem(Long id) {
        return orderItemMapper.toItemResponse(
                orderItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED)));
    }

    public void deleteOrderItems(Long Id) {
        if (!orderItemRepository.existsById(Id)) {
            throw new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED);
        }
        orderItemRepository.deleteById(Id);
    }

}
