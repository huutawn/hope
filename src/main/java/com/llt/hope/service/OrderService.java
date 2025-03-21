package com.llt.hope.service;


import com.llt.hope.dto.request.CartItemUpdateRequest;
import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.request.OrderUpdateRequest;
import com.llt.hope.dto.response.*;
import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.OrderItem;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.OrderMapper;
import com.llt.hope.repository.jpa.OrderRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.DoubleStream.builder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {


    OrderRepository orderRepository;
    UserRepository userRepository;
    OrderMapper orderMapper;

    public OrderResponse createOrder(OrderCreationRequest request){
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_HAS_EXISTED));
        Order order = Order.builder()
                .buyer(buyer)
                .totalAmount(request.getTotalAmount())
                .orderDate(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .status("PENDING")
                .paymentStatus("PENDING")
                .notes(request.getNotes())
                .build();
        order = orderRepository.save(order);
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .buyerId(buyer)
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .build();

        return orderResponse;
    }

    public PageResponse<OrderResponse> getAllOrder(Specification<Order> spec, int page, int size){
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

    public OrderResponse getOrder(Long id){
        return orderMapper.toOrderResponse(
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
        }
        orderRepository.deleteById(orderId);
    }
    public OrderResponse updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        orderMapper.updateOrder(order, request);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
