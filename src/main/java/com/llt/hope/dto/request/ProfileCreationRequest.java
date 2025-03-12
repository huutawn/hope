package com.llt.hope.dto.request;

import com.llt.hope.entity.MediaFile;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
