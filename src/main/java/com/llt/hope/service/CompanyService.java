package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.entity.*;
import com.llt.hope.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CompanyMapper;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CompanyService {
    CompanyRepository companyRepository;
    UserRepository userRepository;
    ProfileRepository profileRepository;
    MediaFileRepository mediaFileRepository;
    CloudinaryService cloudinaryService;
    CompanyMapper companyMapper;
    RoleRepository roleRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public CompanyResponse createCompany(CompanyCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Profile profile = profileRepository
                .findProfileByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        MediaFile mediaFile = null;
        if (request.getCompanyImage() != null) {
            try {
                mediaFile = cloudinaryService.uploadFile(request.getCompanyImage(), "company", email);
                mediaFileRepository.save(mediaFile);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
            }
        }

        Company company = Company.builder()
                .createdAt(LocalDate.now())
                .isActive(false)
                .address(request.getAddress())
                .industry(request.getIndustry())
                .size(request.getSize())
                .name(request.getName())
                .description(request.getDescription())
                .logo(mediaFile)
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .taxCode(request.getTaxCode())
                .build();
        companyRepository.save(company);
        profile.setCompany(company);
        profileRepository.save(profile);
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefindRole.EMPLOYER_ROLE).ifPresent(roles::add);
        user.setProfile(profile);
        user.setRoles(roles);
        userRepository.save(user);
        return companyMapper.toCompanyResponse(company);
    }
    public CompanyResponse getAllCompanyNonActive(){

    }
}
