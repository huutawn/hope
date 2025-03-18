package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SellerProfileCreationRequest {
    String phone;
    String userId;
    String email;
    String storeName;
    String storeDescription;
}
