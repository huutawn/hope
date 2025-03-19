package com.llt.hope.service;


import com.llt.hope.dto.request.PaymentCreationRequest;
import com.llt.hope.dto.response.PaymentResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.Payment;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.PaymentMapper;
import com.llt.hope.repository.jpa.OrderRepository;
import com.llt.hope.repository.jpa.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {

     PaymentRepository paymentRepository;
     OrderRepository orderRepository;
     PaymentMapper paymentMapper;
     @Transactional
     public PaymentResponse createPayment(PaymentCreationRequest request){
         Order order = orderRepository.findById(request.getOrderId())
                 .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

         Payment payment = Payment.builder()
                 .order(order)
                 .paymentMethod(request.getPaymentMethod())
                 .build();

         payment = paymentRepository.save(payment);
         PaymentResponse paymentResponse = PaymentResponse.builder()
                 .paymentId(payment.getPaymentId())
                 .orderId(payment.getOrder())
                 .paymentMethod(payment.getPaymentMethod())
                 .paymentDate(LocalDateTime.now())
                 .paymentStatus("PENDING")
                 .build();
         paymentRepository.deleteByOrder(order);
         orderRepository.delete(order);
         return paymentResponse;
     }
}
