package com.llt.hope.service;

import com.llt.hope.dto.request.ProfileCreationRequest;
import com.llt.hope.dto.request.RecruitmentCreationRequest;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobMapper;
import com.llt.hope.mapper.ProfileMapper;
import com.llt.hope.repository.*;
import com.llt.hope.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

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
   public ProfileResponse createMyProfile(ProfileCreationRequest request){
       String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
       User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
       MediaFile mediaFile;
       try {
            mediaFile = cloudinaryService.uploadFile(request.getProfilePicture(), "profile",email);
           mediaFile = mediaFileRepository.save(mediaFile);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       Profile profile= Profile.builder()
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
       public ProfileResponse getMyProfile(){
       String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
       User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
       Profile profile = profileRepository.findProfileByUser(user).orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
       return profileMapper.toProfileResponse(profile);
   }
}
