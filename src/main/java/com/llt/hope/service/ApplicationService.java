package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.response.*;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobApplicationMapper;
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
public class ApplicationService {
    UserRepository userRepository;
    JobRepository jobRepository;
    JobApplicationRepository jobApplicationRepository;
    JobApplicationMapper jobApplicationMapper;

    public JobApplicationResponse applyJob(long jobId) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setApplicant(user);
        jobApplication.setAppliedAt(LocalDateTime.now());
        jobApplication.setStatus("APPLIED");
        jobApplication = jobApplicationRepository.save(jobApplication);
        JobApplicationResponse jobApplicationResponse = JobApplicationResponse.builder()
                .id(jobApplication.getId())
                .job(jobApplication.getJob())
                .applicant(jobApplication.getApplicant())
                .build();
        return jobApplicationResponse;
    }

    public PageResponse<JobApplicationResponse> getAllApplicantByJob(
            long jobId, Specification<JobApplication> spec, int page, int size) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));
        Sort sort = Sort.by(Sort.Direction.DESC, "appliedAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<JobApplication> jobApplications = jobApplicationRepository.findAllJobApplicationsByJob(job, pageable);
        List<JobApplicationResponse> jobApplicationResponses = jobApplications.getContent().stream()
                .map(jobApplicationMapper::toJobApplicationResponse)
                .toList();

        return PageResponse.<JobApplicationResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(jobApplications.getTotalElements())
                .totalPages(jobApplications.getTotalPages())
                .data(jobApplicationResponses)
                .build();
    }
}
