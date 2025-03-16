package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCategoryResponse {
    Long id;
    String name;
    String description;
    LocalDateTime createdAt;
}
