package com.llt.hope.controller;

import java.math.BigDecimal;

import com.llt.hope.dto.response.StartTransactionResponse;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.SePayWebhookRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.VolunteerResponse;
import com.llt.hope.service.SePayWebHookService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/hooks/sepay-payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
public class SePayWebController {

    SePayWebHookService sePayWebhookService;

    @PostMapping
    public ApiResponse<VolunteerResponse> receiveWebhook(@RequestBody SePayWebhookRequest webhookData) {

        // Xử lý webhook
        return ApiResponse.<VolunteerResponse>builder()
                .result(sePayWebhookService.handleWebhook(webhookData))
                .build();
    }

    @GetMapping
    public ApiResponse<BigDecimal> getFundBalance() {
        return ApiResponse.<BigDecimal>builder()
                .result(sePayWebhookService.getFundBalance())
                .build();
    }

    @GetMapping("/start")
    public ApiResponse<StartTransactionResponse> startTransaction() {
        return ApiResponse.<StartTransactionResponse>builder()
                .result(sePayWebhookService.startTransaction())
                .build();
    }
    
}
