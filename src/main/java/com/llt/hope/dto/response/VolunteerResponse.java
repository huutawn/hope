package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.llt.hope.entity.FundBalance;
import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerResponse {
    long id;
    User user;
    private String transactionId;
    private BigDecimal amount;
    private String content;
    private String accountNumber;
    private String transactionDate;
    private String description;
    private String referenceNumber;
    private LocalDateTime createdAt;
    private FundBalance fundBalance;
}
