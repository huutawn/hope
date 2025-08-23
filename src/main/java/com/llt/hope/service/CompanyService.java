package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import com.llt.hope.dto.response.ActiveCompanyResponse;
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
    ResendEmailService resendEmailService;
    EmailService emailService;

    String website = "https://hopevn.site/dashboard";

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
                log.info("file" + mediaFile.getUrl());
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
                .email(request.getEmail())
                .description(request.getDescription())
                .logo(mediaFile)
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .taxCode(request.getTaxCode())
                .build();

        company = companyRepository.saveAndFlush(company);
        profile.setCompany(company);
        profile = profileRepository.saveAndFlush(profile);
        user.setProfile(profile);
        userRepository.save(user);
        companyRepository.save(company);
        return companyMapper.toCompanyResponse(company);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
    public ActiveCompanyResponse activeCompany(long companyId) {
        log.info("company id: " + companyId);
        Optional<Company> company = companyRepository.findById(companyId);
        Company company1 = company.get();
        if (company1.isActive()) {
            throw new AppException(ErrorCode.COMPANY_ALREADY_ACTIVE);
        }
        company.get().setActive(true);
        Optional<Profile> profile = profileRepository.findProfileByCompany(company1);
        Profile profile1;
        if (profile.isPresent()) {
            profile1 = profile.get();
        } else {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }

        company1 = companyRepository.save(company1);
        profile1.setCompany(company1);
        profileRepository.save(profile1);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefindRole.EMPLOYER_ROLE).ifPresent(roles::add);
        User user = userRepository
                .findUserByProfile(profile1)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setRoles(roles);
        user = userRepository.save(user);
//        String subject = " Your company completely accepted";
//        String content = String.format(
//                "<p><strong>Xin chào %s,</strong></p>"
//                        + "<p>Chúng tôi rất vui mừng thông báo rằng công ty của bạn đã chính thức được phê duyệt trên nền tảng <strong>Hope</strong>!</p>"
//                        + "<p>Giờ đây, bạn có toàn quyền truy cập vào bảng điều khiển nhà tuyển dụng, nơi bạn có thể:</p>"
//                        + "<ul>"
//                        + "<li>🎯 Đăng tin tuyển dụng nhanh chóng và tiếp cận hàng ngàn ứng viên tài năng.</li>"
//                        + "<li>🔍 Quản lý hồ sơ ứng viên dễ dàng với công cụ lọc và sàng lọc thông minh.</li>"
//                        + "<li>📊 Theo dõi hiệu quả tuyển dụng với các thống kê chi tiết.</li>"
//                        + "</ul>"
//                        + "<p>Hãy bắt đầu ngay bằng cách truy cập vào trang quản lý của bạn:</p>"
//                        + "<h2><a href='%s' style='color: #007bff; text-decoration: none;'>👉 Đăng nhập & Quản lý tuyển dụng</a></h2>"
//                        + "<p>Chúng tôi cam kết mang lại trải nghiệm tốt nhất cho doanh nghiệp của bạn, giúp bạn tìm kiếm và kết nối với những ứng viên phù hợp nhất.</p>"
//                        + "<p>Nếu có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với đội ngũ hỗ trợ của chúng tôi.</p>"
//                        + "<p>Chúc bạn thành công và xây dựng một đội ngũ xuất sắc!</p>"
//                        + "<p><strong>Trân trọng,</strong><br/>Đội ngũ Hope</p>",
//                company1.getName(), website);
//        emailService.sendEmail(user.getEmail(), subject, content);
        ActiveCompanyResponse activeCompanyResponse = ActiveCompanyResponse.builder()
                .id(company1.getId())
                .isActive(company1.isActive())
                .name(company1.getName())
                .build();
        return activeCompanyResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCompany(Long companyId) {
        Company company =
                companyRepository.findById(companyId).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));

        Profile profile = profileRepository
                .findProfileByCompany(company)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        User user =
                userRepository.findUserByProfile(profile).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefindRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        userRepository.saveAndFlush(user);

        profile.setCompany(null);
        profileRepository.saveAndFlush(profile);

        companyRepository.deleteById(companyId);
    }
    public CompanyResponse getDetail(Long id){
        Company company=companyRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.COMPANY_NOT_FOUND));
        return companyMapper.toCompanyResponse(company);
    }
}
