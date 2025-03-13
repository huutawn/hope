package com.llt.hope.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductUpdateRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotBlank
    private String description;

    @NotNull
    private Long inventory;

    private Boolean isFeatured;
    MultipartFile imageFile;
}
