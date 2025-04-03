package com.llt.hope.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationResponse {
    long postId;
    String donatorId;
    BigDecimal amount;
    BigDecimal totalAmount;
}
