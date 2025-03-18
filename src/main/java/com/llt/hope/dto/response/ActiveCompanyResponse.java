package com.llt.hope.dto.response;

import com.llt.hope.entity.MediaFile;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
