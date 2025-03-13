package com.llt.hope.service;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.RecruitmentCreationRequest;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.JobCategory;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobMapper;
import com.llt.hope.repository.JobCategoryRepository;
import com.llt.hope.repository.JobRepository;
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
public class JobService {
    JobRepository jobRepository;
    UserRepository userRepository;
    JobCategoryRepository jobCategoryRepository;
    JobMapper jobMapper;

    @PreAuthorize("isAuthenticated()")
    public JobResponse createRecruitmentNews(RecruitmentCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var employer = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        JobCategory jobCategory = jobCategoryRepository
                .findJobCategoryByName(request.getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (request.getTitle().isEmpty()) throw new AppException((ErrorCode.TITLE_INVALID));
        Job job = Job.builder()
                .createdAt(LocalDateTime.now())
                .jobCategory(jobCategory)
                .applicationDeadline(request.getApplicationDeadline())
                .benefits(request.getBenefits())
                .description(request.getDescription())
                .employer(employer)
                .title(request.getTitle())
                .views(0)
                .location(request.getLocation())
                .salaryMax(request.getSalaryMax())
                .salaryMin(request.getSalaryMin())
                .requirements(request.getRequirements())
                .suitableForDisability(request.getSuitableForDisability())
                .responsibilities(request.getResponsibilities())
                .build();
        return jobMapper.toJobResponse(jobRepository.save(job));
    }
}
