package com.llt.hope.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.llt.hope.dto.response.PaymentResponse;
import com.llt.hope.dto.response.ResponseObject;
import com.llt.hope.service.OrderService;
import com.llt.hope.service.PaymentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VnPayController {
    PaymentService paymentService;
    OrderService orderService;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentResponse> pay(@RequestParam Long orderId, HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(orderId, request));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderIdStr = request.getParameter("vnp_TxnRef"); // Lấy Order ID từ callback VNPAY

        if (status.equals("00")) { // Nếu thanh toán thành công
            Long orderId = Long.parseLong(orderIdStr);
            orderService.updateOrderAfterPayment(orderId); // Cập nhật đơn hàng

            return new ResponseObject<>(HttpStatus.OK, "Payment successful", new PaymentResponse("00", "Success", ""));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Payment failed", null);
        }
    }
}
