package com.llt.hope.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.llt.hope.entity.User;
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
        @NotBlank
        String seller_id;
        @NotNull
        @DecimalMin("0.0")
        BigDecimal price;

        @NotBlank
        String description;

        @NotNull
        Long categoryId;

        @NotNull
        int inventory;

        String creationProcess;
        String materialsUsed;
        BigDecimal weight;
        String dimensions;
        List<MultipartFile> imagesFile;
    }
