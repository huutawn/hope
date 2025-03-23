package com.llt.hope.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    @NotBlank(message = "Email can not be blank")
    private String toEmail;

    @NotBlank(message = "Subject can not be blank")
    private String subject;

    @NotBlank(message = "Body can not be blank")
    private String body;
}
