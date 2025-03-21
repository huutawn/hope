package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.OrderCreationRequest;
import com.llt.hope.dto.response.OrderResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.OrderMapper;
import com.llt.hope.repository.jpa.OrderRepository;
import com.llt.hope.repository.jpa.UserRepository;

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

    public OrderResponse createOrder(OrderCreationRequest request) {
        User buyer = userRepository
                .findById(request.getBuyerId())
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

    public List<OrderResponse> getAllOrder() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    response.setOrderId(order.getId());
                    response.setBuyerId(order.getBuyer());
                    response.setOrderDate(LocalDateTime.now());
                    response.setStatus(order.getStatus());
                    response.setTotalAmount(order.getTotalAmount());
                    response.setPaymentMethod(order.getPaymentMethod());
                    response.setPaymentStatus(order.getPaymentStatus());
                    response.setNotes(order.getNotes());
                    return response;
                })
                .toList();
    }

    public OrderResponse getOrder(Long id) {
        return orderMapper.toOrderResponse(
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }

    public void deleteProduct(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        orderRepository.deleteById(orderId);
    }
}
