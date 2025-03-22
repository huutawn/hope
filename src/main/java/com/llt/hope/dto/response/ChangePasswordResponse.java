package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordResponse {
    private boolean isSuccess;
    private String message;
}
