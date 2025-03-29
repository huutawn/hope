package com.llt.hope.controller;

import com.llt.hope.dto.request.SePayWebhookRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.VolunteerResponse;
import com.llt.hope.entity.FundBalance;
import com.llt.hope.entity.Transaction;
import com.llt.hope.repository.jpa.FundBalanceRepository;
import com.llt.hope.repository.jpa.TransactionRepository;
import com.llt.hope.service.SePayWebHookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RequestMapping("/api/hooks/sepay-payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
public class SePayWebController {

    SePayWebHookService sePayWebhookService;
    @PostMapping
    public ApiResponse<VolunteerResponse> receiveWebhook(
            @RequestBody SePayWebhookRequest webhookData)
             {

        // Xử lý webhook
        return ApiResponse.<VolunteerResponse>builder()
                .result(sePayWebhookService.handleWebhook(webhookData))
                .build();

    }
    @GetMapping
public ApiResponse<Double> getFundBalance(){
    return ApiResponse.<Double>builder()
            .result(sePayWebhookService.getFundBalance())
            .build();
    }
}
