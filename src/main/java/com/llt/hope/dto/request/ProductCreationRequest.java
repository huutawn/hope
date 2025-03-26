package com.llt.hope.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductCreationRequest {
    @Size(max = 100)
    String name;

    @DecimalMin("0.0")
    BigDecimal price;

    String description;

    int inventory;

    String dimensions;
    List<MultipartFile> imagesFile;
}
