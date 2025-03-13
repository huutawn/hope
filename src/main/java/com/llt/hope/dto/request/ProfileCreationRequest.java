package com.llt.hope.dto.request;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreationRequest {
    private String fullName;
    private String disabilityType;
    private String disabilityDescription;
    private String address;
    private String city;
    private String phone;
    private LocalDate dob;

    private String gender;

    private MultipartFile profilePicture;

    private String bio;
}
