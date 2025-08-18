package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CVUpdateReq {
    Long cvId;
    String name;
    String phone;
    String email;
    String address;
    LocalDate dob;
    String skill;
    String exp; //kinh nghiệm
    String education;
    String typeOfDisability; //loại khuyết tật
    String typeOfJob; //loại công việc mong muốn
}
