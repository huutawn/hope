package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.entity.Company;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.Profile;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CompanyMapper;
import com.llt.hope.repository.CompanyRepository;
import com.llt.hope.repository.MediaFileRepository;
import com.llt.hope.repository.ProfileRepository;
import com.llt.hope.repository.UserRepository;
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

    @Transactional
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
        return companyMapper.toCompanyResponse(company);
    }
}
