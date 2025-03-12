package com.llt.hope.dto.response;

import com.llt.hope.entity.MediaFile;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
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
