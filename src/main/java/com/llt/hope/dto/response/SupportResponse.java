package com.llt.hope.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportResponse {
    private Long id;
    private String userId;
    private String userName;
    private String userEmail;
    private Long postId;
    private String postTitle;
    private BigDecimal donatedMoney;
    private LocalDateTime donatedAt;
}
