package com.llt.hope.service;


import com.llt.hope.dto.request.SePayWebhookRequest;
import com.llt.hope.dto.response.VolunteerResponse;
import com.llt.hope.entity.FundBalance;
import com.llt.hope.entity.Transaction;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.FundBalanceRepository;
import com.llt.hope.repository.jpa.TransactionRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SePayWebHookService {

    TransactionRepository transactionRepository;
    FundBalanceRepository fundBalanceRepository;
    UserRepository userRepository;

    public VolunteerResponse handleWebhook(SePayWebhookRequest webhookData) {


        // Chỉ xử lý nếu tiền vào (transferType = "in")
        if (!"in".equalsIgnoreCase(webhookData.getTransferType())) {
            log.warn("⛔ Bỏ qua giao dịch tiền ra: {}", webhookData);
            return null;
        }

        // Tạo bản ghi giao dịch
        Transaction transaction = new Transaction();
        transaction.setTransactionId(webhookData.getId().toString());
        transaction.setAccountNumber(webhookData.getAccountNumber());
        transaction.setTransactionDate(webhookData.getTransactionDate());
        transaction.setAmount(webhookData.getTransferAmount());
        transaction.setContent(webhookData.getContent());
        transaction.setDescription(webhookData.getContent());
        transaction.setReferenceNumber(webhookData.getReferenceCode());

        transaction=transactionRepository.save(transaction);
        log.info("✅ Giao dịch đã được lưu vào database: {}", transaction);

        // Cập nhật số dư quỹ chung
        FundBalance fundBalance = fundBalanceRepository.findById(1L).orElse(new FundBalance());
        fundBalance.setBalance(fundBalance.getBalance().add(transaction.getAmount()));
        fundBalance=fundBalanceRepository.save(fundBalance);
        log.info("💰 Cập nhật số dư quỹ chung: {}", fundBalance.getBalance());

        VolunteerResponse volunteerResponse = VolunteerResponse.builder()
                .id(transaction.getId())
                .accountNumber(transaction.getAccountNumber())
                .transactionDate(transaction.getTransactionDate())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .content(transaction.getContent())
                .referenceNumber(transaction.getReferenceNumber())
                .fundBalance(fundBalance)
                .build();
        return volunteerResponse;
    }
    public BigDecimal getFundBalance(){
        FundBalance fundBalance = fundBalanceRepository.findById(1L).orElse(new FundBalance());
        return fundBalance.getBalance();
    }

}
