package com.llt.hope.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVFormResponse {
    private Long id;

    String userName;
    String userId;
    String name;
    String phone;
    String email;
    String address;
    LocalDate dob;
    String skill;
    String exp; // kinh nghiệm
    String education;
    String typeOfDisability; // loại khuyết tật
    String typeOfJob; // loại công việc mong muốn
    LocalDateTime createdAt;
}
