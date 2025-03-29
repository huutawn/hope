package com.llt.hope.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductUpdateRequest {
    @Size(max = 100)
    private String name;

    @DecimalMin("0.0")
    private BigDecimal price;

    private String description;

    private Long inventory;

    MultipartFile imageFile;
}
