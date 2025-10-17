package com.llt.hope.dto.request;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessWithdrawalRequest {
    Long withdrawalRequestId;
    String status; // e.g., StatusCons.WITHDRAWAL_APPROVED, StatusCons.WITHDRAWAL_REJECTED
    BigDecimal approvedAmount; // Required for approval
    String reason; // Required for rejection
}
