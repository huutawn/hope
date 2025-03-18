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
public class SellerProfileUpdateRequest {
    String phone;
    String email;
    String storeName;
    String storeDescription;
    Boolean active;
}
