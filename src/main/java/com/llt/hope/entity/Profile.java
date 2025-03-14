package com.llt.hope.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;
    private String disabilityType;
    private String disabilityDescription;
    private String address;
    private String city;
    private String country = "Vietnam";
    private String phone;
    private LocalDate dob;

    private String gender;

    @OneToOne
    @JoinColumn(name = "file_id")
    private MediaFile profilePicture;

    @OneToOne
    @JoinColumn(name = "company_id") // Nhà tuyển dụng đã được xác minh chưa?
    private Company company;

    private String bio;
}
