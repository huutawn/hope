package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SePayWebhookRequest {
    private Long id; // ID giao dịch trên SePay
    private String gateway; // Brand name của ngân hàng
    private String transactionDate; // Thời gian xảy ra giao dịch
    private String accountNumber; // Số tài khoản ngân hàng
    private String code; // Mã code thanh toán (có thể null)
    private String content; // Nội dung chuyển khoản
    private String transferType; // Loại giao dịch (in = tiền vào, out = tiền ra)
    private BigDecimal transferAmount; // Số tiền giao dịch
    private BigDecimal accumulated; // Số dư tài khoản lũy kế
    private String subAccount; // Tài khoản phụ (có thể null)
    private String referenceCode; // Mã tham chiếu của ngân hàng
    private String description; // Toàn bộ nội dung tin nhắn sms
}
