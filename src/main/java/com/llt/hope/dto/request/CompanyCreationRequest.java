package com.llt.hope.dto.request;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyCreationRequest {
    private String name; // Tên công ty

    private String description; // Giới thiệu công ty

    private String industry; // Ngành nghề chính

    private String website;

    private String phoneNumber; // Số điện thoại liên hệ

    private String email; // Email liên hệ

    private String address; // Địa chỉ công ty

    private String size; // Quy mô công ty (Small, Medium, Large)

    private MultipartFile companyImage;

    private String taxCode; // Mã số thuế (nếu có)

    private LocalDate createdAt; // Ngày tạo công ty

    private LocalDate updatedAt;
}
