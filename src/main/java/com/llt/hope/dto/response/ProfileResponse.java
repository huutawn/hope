package com.llt.hope.dto.response;

import java.time.LocalDate;

import com.llt.hope.entity.MediaFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String fullName;
    private String disabilityType;
    private String disabilityDescription;
    private String address;
    private String city;
    private String country;
    private String phone;
    private LocalDate dob;

    private String gender;

    private MediaFile profilePicture;

    private String bio;
}
