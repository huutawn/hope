package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonationQRResponse {
    private String qrCodeUrl;
    private String userCode;
    private String content;
    private String message;
}
