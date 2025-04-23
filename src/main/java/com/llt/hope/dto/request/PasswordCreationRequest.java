package com.llt.hope.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordCreationRequest {
    @NotBlank(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "INVALID_OTP")
    String otp;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
}
