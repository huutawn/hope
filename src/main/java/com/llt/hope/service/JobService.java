package com.llt.hope.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.llt.hope.entity.JobCategory;
import com.llt.hope.entity.User;
import com.llt.hope.repository.jpa.JobCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.RecruitmentCreationRequest;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Job;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobHandlerMapper;
import com.llt.hope.mapper.JobMapper;
import com.llt.hope.repository.jpa.JobRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.specification.JobSpecification;
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
    JobMapper jobMapper;
    JobHandlerMapper jobHandlerMapper;
    JobCategoryRepository jobCategoryRepository;

    @PreAuthorize("isAuthenticated()")
    public JobResponse createRecruitmentNews(RecruitmentCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var employer = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        log.info(employer.getProfile().getCountry());
        if (employer.getProfile().getCompany() == null) {
            throw new AppException(ErrorCode.COMPANY_NOT_FOUND);
        }
        if (!employer.getProfile().getCompany().isActive()) {
            throw new AppException(ErrorCode.COMPANY_IS_NOT_ACTIVE);
        }
        JobCategory jobCategory =jobCategoryRepository.findById(request.getCategoryId())
                .orElseGet(()->jobCategoryRepository.save(JobCategory.builder()
                                .name("khác")
                        .build()));

        if (request.getTitle().isEmpty()) throw new AppException((ErrorCode.TITLE_INVALID));
        Job job = Job.builder()
                .createdAt(LocalDateTime.now())
                .applicationDeadline(request.getApplicationDeadline())
                .benefits(request.getBenefits())
                .description(request.getDescription())
                .employer(employer)
                .title(request.getTitle())
                .views(0)
                .company(employer.getProfile().getCompany())
                .jobCategory(jobCategory)
                .location(request.getLocation())
                .salaryMax(request.getSalaryMax())
                .salaryMin(request.getSalaryMin())
                .requirements(request.getRequirements())
                .suitableForDisability(request.getSuitableForDisability())
                .responsibilities(request.getResponsibilities())
                .build();
        Job savedJob = jobRepository.save(job);

        return jobHandlerMapper.toJobResponse(savedJob);
    }

    public PageResponse<JobResponse> getAllJobRecruitments(Specification<Job> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Job> jobs = jobRepository.findAll(spec, pageable);
        List<JobResponse> jobResponses =
                jobs.getContent().stream().map(jobHandlerMapper::toJobResponse).toList();
        return PageResponse.<JobResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(jobs.getTotalElements())
                .totalPages(jobs.getTotalPages())
                .data(jobResponses)
                .build();
    }
    public PageResponse<JobResponse> getAllJobByCompany(Specification<Job> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        User user=userRepository.findByEmail(SecurityUtils.getCurrentUserLogin().get())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        Page<Job> jobs = jobRepository.findAllByCompany(user.getProfile().getCompany(), pageable);
        List<JobResponse> jobResponses =
                jobs.getContent().stream().map(jobHandlerMapper::toJobResponse).toList();
        return PageResponse.<JobResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(jobs.getTotalElements())
                .totalPages(jobs.getTotalPages())
                .data(jobResponses)
                .build();
    }



    @PreAuthorize("isAuthenticated()")
    public JobResponse getDetail(Long id){
        Job job=jobRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.JOB_NOT_FOUND));
        Integer view= job.getViews();
        if(view==null)
            view=0;
        job.setViews(view+1);
        job=jobRepository.save(job);
        return jobHandlerMapper.toJobResponse(job);
    }


    public PageResponse<JobResponse> filterJobs(
            String categoryName, String requirement, BigDecimal minSalary, BigDecimal maxSalary, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Specification<Job> spec = Specification.where(JobSpecification.hasCategory(categoryName))
                .and(JobSpecification.hasRequirements(requirement))
                .and(JobSpecification.salaryBetween(minSalary, maxSalary));

        Page<Job> jobs = jobRepository.findAll(spec, pageable);
        List<JobResponse> jobResponses =
                jobs.getContent().stream().map(jobHandlerMapper::toJobResponse).toList();

        return PageResponse.<JobResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(jobs.getTotalElements())
                .totalPages(jobs.getTotalPages())
                .data(jobResponses)
                .build();
    }

    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào 00:00 mỗi ngày
    @Transactional
    public void deleteExpiredJobs() {
        LocalDateTime now = LocalDateTime.now();
        jobRepository.deleteByDeadlineBefore(now);
    }
}
