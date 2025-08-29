package com.llt.hope.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CVForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    User user;

    String name;
    String phone;
    String email;
    @Column(columnDefinition = "TEXT")
    String address;
    LocalDate dob;
    @Column(columnDefinition = "TEXT")
    String skill;
    @Column(columnDefinition = "TEXT")
    String exp; //kinh nghiệm
    String education;
    String typeOfDisability;
    @Column(columnDefinition = "TEXT")//loại khuyết tật
    String typeOfJob; //loại công việc mong muốn
    LocalDateTime createdAt;

}
