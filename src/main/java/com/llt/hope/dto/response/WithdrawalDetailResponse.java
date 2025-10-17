package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalDetailResponse {
    Long id;
    BigDecimal requestedAmount;
    BigDecimal approvedAmount;
    String status;
    String reason;
    LocalDateTime requestedAt;
    LocalDateTime processedAt;
    String processedBy;
    String bankAccount;
    String bankName;
    String accountHolderName;
    PostVolunteerResponse postVolunteer;
    UserResponse user;
}
