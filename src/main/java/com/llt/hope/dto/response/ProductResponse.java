package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    Long id;
    String name;
    BigDecimal price;
    String description;
    ProductCategory productCategory;
    User seller;
    int inventory;
    String dimensions;
    Set<MediaFile> images;
    LocalDateTime createdAt;
}
