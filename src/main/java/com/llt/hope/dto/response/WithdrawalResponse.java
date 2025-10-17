package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalResponse {
    private Long id;
    private Long postVolunteerId;
    private String userId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private String status;
    private String reason;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private String bankAccount;
    private String bankName;
    private String accountHolderName;
}
