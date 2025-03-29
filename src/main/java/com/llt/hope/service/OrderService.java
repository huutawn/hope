package com.llt.hope.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.llt.hope.dto.request.OrderItemCreationRequest;
import com.llt.hope.entity.OrderItem;
import com.llt.hope.mapper.OrderItemsMapper;
import com.llt.hope.repository.jpa.OrderItemRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.request.OrderUpdateRequest;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.OrderMapper;
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
public class OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    OrderMapper orderMapper;
    ProductRepository productRepository;
    OrderItemRepository orderItemRepository;
    OrderItemsMapper orderItemsMapper;

    public OrderResponse createOrder(OrderCreationRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo đơn hàng trước
        Order order = Order.builder()
                .buyer(user)
                .createdAt(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .status("PENDING")
                .paymentStatus("PENDING")
                .notes(request.getNotes())
                .totalAmount(BigDecimal.ZERO)
                .build();
        order = orderRepository.save(order); // 🔥 LƯU ORDER TRƯỚC

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemCreationRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            BigDecimal itemPrice = product.getPrice();
            if (itemPrice == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            BigDecimal itemTotal = itemPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // Gán order vào orderItem để tránh lỗi "order_id cannot be null"
            OrderItem orderItem = OrderItem.builder()
                    .order(order) // 🔥 BẮT BUỘC PHẢI CÓ ORDER Ở ĐÂY
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(itemPrice)
                    .subTotal(itemTotal)
                    .build();
            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems); // 🔥 Lưu danh sách order items
        order.setTotalAmount(totalAmount);
        orderRepository.save(order); // 🔥 Cập nhật tổng tiền

        return OrderResponse.builder()
                .orderId(order.getId())
                .buyerId(user.getId())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .build();
    }

    public PageResponse<OrderResponse> getAllOrder(Specification<Order> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses =
                orders.getContent().stream().map(orderMapper::toOrderResponse).toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .data(orderResponses)
                .build();
    }

    public OrderResponse getOrder(Long id) {
        return orderMapper.toOrderResponse(
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderUpdateRequest request) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));

        // Cập nhật trạng thái
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order); // Convert Order -> OrderResponse
    }

    @Transactional
    public void updateOrderAfterPayment(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Cập nhật trạng thái đơn hàng
        order.setPaymentStatus("PAID");
        order.setStatus("DELIVERED"); // Hoặc PROCESSING, tùy vào quy trình của bạn

        orderRepository.save(order); // Lưu thay đổi vào database
    }
}
