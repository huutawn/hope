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
public class ProductCreationRequest {
    @NotBlank
    @Size(max = 100)
    String name;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal price;

    @NotBlank
    String description;

    @NotNull
    Long categoryId;

    @NotNull
    Long inventory;

    String creationProcess;
    String materialsUsed;
    String productionTime;
    BigDecimal weight;
    String dimensions;
    Boolean isCustomizable;
    Boolean isFeatured;
    MultipartFile imageFile;
}
