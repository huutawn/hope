package com.llt.hope.service;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.ProfileCreationRequest;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProfileMapper;
import com.llt.hope.repository.*;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {
    ProfileRepository profileRepository;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    ProfileMapper profileMapper;
    MediaFileRepository mediaFileRepository;

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ProfileResponse createMyProfile(ProfileCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        MediaFile mediaFile = null;
        if (request.getProfilePicture() != null) {
            try {
                mediaFile = cloudinaryService.uploadFile(request.getProfilePicture(), "profile", email);
                mediaFile = mediaFileRepository.save(mediaFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Profile profile = Profile.builder()
                .city(request.getCity())
                .bio(request.getBio())
                .address(request.getAddress())
                .dob(request.getDob())
                .disabilityType(request.getDisabilityType())
                .disabilityDescription(request.getDisabilityDescription())
                .fullName(request.getFullName())
                .gender(request.getGender())
                .user(user)
                .profilePicture(mediaFile)
                .build();
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @PreAuthorize("isAuthenticated()")
    public ProfileResponse getMyProfile() {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Profile profile = profileRepository
                .findProfileByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        return profileMapper.toProfileResponse(profile);
    }

    public Profile createInitProfile(String email,String phone, String fullName){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Profile profile = Profile.builder()
               .city("")
               .bio("")
               .address("")
               .dob(null)
               .disabilityType("")
               .disabilityDescription("")
               .fullName(fullName)
               .gender("")
               .phone(phone)
               .user(user)
               .build();
        return profileRepository.save(profile);
    }
}
