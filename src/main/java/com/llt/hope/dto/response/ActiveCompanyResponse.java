package com.llt.hope.dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveCompanyResponse {
    private Long id;
    private String name; // Tên công ty

    private boolean isActive;
    private LocalDate updatedAt;
}
