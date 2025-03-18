package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerProfileResponse {
    String phone;
    String email;
    String storeName;
    String storeDescription;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
