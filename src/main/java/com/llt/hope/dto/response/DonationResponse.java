package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
