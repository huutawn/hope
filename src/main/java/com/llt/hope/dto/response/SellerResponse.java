package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerResponse {
    Long id;
    String phone;
    String email;
    String storeName;
    String storeDescription;
    boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
