//package com.llt.hope.controller;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.llt.hope.dto.request.PaymentCreationRequest;
//import com.llt.hope.dto.response.ApiResponse;
//import com.llt.hope.dto.response.PaymentResponse;
//import com.llt.hope.service.PaymentService;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/payment")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
//@Slf4j
//public class PaymentController {
//
//    PaymentService paymentService;
//
//    @PostMapping
//    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentCreationRequest request) {
//        return ApiResponse.<PaymentResponse>builder()
//                .result(paymentService.createPayment(request))
//                .build();
//    }
//}
