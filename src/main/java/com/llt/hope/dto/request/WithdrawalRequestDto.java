package com.llt.hope.dto.request;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequestDto {
    private Long postVolunteerId;
    private BigDecimal requestedAmount;
    private String bankAccount;
    private String bankName;
    private String accountHolderName;
}
