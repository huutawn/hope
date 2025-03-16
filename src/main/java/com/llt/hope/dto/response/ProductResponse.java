package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.llt.hope.entity.MediaFile;

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
    String category;
    String seller_id;
    Long inventory;
    List<MediaFile> images;
    LocalDateTime createdAt;
}
