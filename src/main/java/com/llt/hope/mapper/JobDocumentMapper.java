package com.llt.hope.mapper;

import org.springframework.stereotype.Component;

import com.llt.hope.document.elasticsearch.JobDocument;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobDocumentMapper {
    private final UserRepository userRepository;

    public JobResponse toJobResponse(JobDocument jobDocument) {
        if (jobDocument == null) {
            return null;
        }

        return JobResponse.builder()
                .id(Long.parseLong(jobDocument.getId()))
                .title(jobDocument.getTitle())
                .employer(userRepository
                        .findById(jobDocument.getEmployerId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .description(jobDocument.getDescription())
                .requirements(jobDocument.getRequirements())
                .responsibilities(jobDocument.getResponsibilities())
                .benefits(jobDocument.getBenefits())
                .suitableForDisability(jobDocument.getSuitableForDisability())
                .location(jobDocument.getLocation())
                .salaryMin(jobDocument.getSalaryMin())
                .salaryMax(jobDocument.getSalaryMax())
                .applicationDeadline(jobDocument.getApplicationDeadline())
                .isActive(jobDocument.getIsActive())
                .createdAt(jobDocument.getCreatedAt())
                .views(jobDocument.getViews())
                .build();
    }
}
