package com.llt.hope.service;

import com.llt.hope.dto.request.JobCategoryCreationRequest;
import com.llt.hope.dto.request.PermissionRequest;
import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.dto.response.PermissionResponse;
import com.llt.hope.entity.JobCategory;
import com.llt.hope.entity.Permission;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobCategoryMapper;
import com.llt.hope.mapper.PermissionMapper;
import com.llt.hope.repository.JobCategoryRepository;
import com.llt.hope.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JobCategoryService {
    JobCategoryMapper jobCategoryMapper;
   JobCategoryRepository jobCategoryRepository;
    public JobCategoryResponse createJobCategory(JobCategoryCreationRequest request){
        if(jobCategoryRepository.existsJobCategoryByName(request.getName()))
            throw new AppException(ErrorCode.CATEGORY_HAS_EXISTED);
        JobCategory jobCategory = JobCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return jobCategoryMapper.toJobCategoryResponse(jobCategoryRepository.save(jobCategory));
    }
}
