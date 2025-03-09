package com.llt.hope.entity;

import java.time.LocalDate;

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
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;
    private String disabilityType;
    private String disabilityDescription;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country = "Vietnam";
    private String phone;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    private MediaFile profilePicture;

    private String bio;
    private String skills;
}

enum Gender {
    MALE,
    FEMALE,
    OTHER
}
