package com.llt.hope.mapper;

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
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .applicationDeadline(job.getApplicationDeadline())
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .views(job.getViews())
                .build();
    }
}
