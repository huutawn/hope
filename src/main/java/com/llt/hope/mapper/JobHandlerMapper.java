package com.llt.hope.mapper;

import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.entity.JobCategory;
import jdk.jfr.Category;
import org.springframework.stereotype.Component;

import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.entity.Job;

@Component
public class JobHandlerMapper {

    public JobResponse toJobResponse(Job job) {
        if (job == null) {
            return null;
        }

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .employer(job.getEmployer())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .benefits(job.getBenefits())
                .suitableForDisability(job.getSuitableForDisability())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .jobCategory(toJobCategoryResponse(job.getJobCategory()))
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .jobCategory(toJobCategoryResponse(job.getJobCategory()))
                .applicationDeadline(job.getApplicationDeadline())
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .views(job.getViews())
                .build();
    }
    private JobCategoryResponse toJobCategoryResponse(JobCategory jobCategory){
        return JobCategoryResponse.builder()
                .name(jobCategory.getName())
                .id(jobCategory.getId())
                .description(jobCategory.getDescription())
                .createdAt(jobCategory.getCreatedAt())
                .build();
    }
}
