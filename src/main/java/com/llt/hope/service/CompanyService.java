package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.request.CompanyCreationRequest;
import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CompanyMapper;
import com.llt.hope.repository.jpa.*;
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
                .isActive(false)
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
        user.setProfile(profile);
        userRepository.save(user);
        return companyMapper.toCompanyResponse(company);
    }

    @PreAuthorize("hasRole('ADMIN")
    public PageResponse<CompanyResponse> getAllCompanyNonActive(Specification<Company> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Company> companies = companyRepository.findCompanyByIsActive(false, pageable);
        List<CompanyResponse> companyResponses = companies.getContent().stream()
                .map(companyMapper::toCompanyResponse) // Chuyển từng Job thành JobResponse
                .toList();
        return PageResponse.<CompanyResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(companies.getTotalElements())
                .totalPages(companies.getTotalPages())
                .data(companyResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CompanyResponse activeCompany(long companyId) {
        Company company =
                companyRepository.findById(companyId).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));
        if (company.isActive()) {
            throw new AppException(ErrorCode.COMPANY_ALREADY_ACTIVE);
        }
        company.setActive(true);
        Profile profile = profileRepository
                .findProfileByCompany(company)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));
        company = companyRepository.save(company);
        profile.setCompany(company);
        profileRepository.save(profile);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefindRole.USER_ROLE).ifPresent(roles::add);
        User user = userRepository
                .findUserByProfile(profile)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setRoles(roles);
        return companyMapper.toCompanyResponse(company);
    }
}
