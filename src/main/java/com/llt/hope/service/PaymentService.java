package com.llt.hope.service;

import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.llt.hope.configuration.VNPayConfig;
import com.llt.hope.dto.response.PaymentResponse;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.Payment;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.OrderRepository;
import com.llt.hope.repository.jpa.PaymentRepository;
import com.llt.hope.utils.VNPayUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentResponse createVnPayPayment(Long orderId, HttpServletRequest request) {
        Order order =
                orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        if (!order.getPaymentStatus().equals("PENDING")) {
            throw new AppException(ErrorCode.ORDER_ALREADY_PAID);
        }

        long amount = order.getTotalAmount().longValue() * 100L;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Tạo URL thanh toán VNPay
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

    public void processVNPayCallback(Map<String, String> params) {
        String orderIdStr = params.get("vnp_TxnRef");
        String paymentStatus = params.get("vnp_TransactionStatus");

        Long orderId = Long.parseLong(orderIdStr);
        Order order =
                orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        if ("00".equals(paymentStatus)) {
            order.setPaymentStatus("PAID");
        } else {
            order.setPaymentStatus("FAILED");
        }

        orderRepository.save(order);

        Payment payment = Payment.builder()
                .transactionId(params.get("vnp_TransactionNo"))
                .amount(Double.parseDouble(params.get("vnp_Amount")) / 100)
                .status(order.getPaymentStatus())
                .order(order)
                .build();

        paymentRepository.save(payment);
    }
}
