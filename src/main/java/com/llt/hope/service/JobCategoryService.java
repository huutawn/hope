package com.llt.hope.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.JobCategoryCreationRequest;
import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.entity.JobCategory;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobCategoryMapper;
import com.llt.hope.repository.jpa.JobCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JobCategoryService {
    JobCategoryMapper jobCategoryMapper;
    JobCategoryRepository jobCategoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public String createJobCategory(List<JobCategoryCreationRequest> request) {
      List<JobCategory> jobCategory=new ArrayList<>();
      for(JobCategoryCreationRequest jobCategory1:request){
          JobCategory jobCategory2=JobCategory.builder()
                  .name(jobCategory1.getName())
                  .description(jobCategory1.getDescription())
                  .build();
          jobCategory.add(jobCategory2);
      }
        jobCategoryRepository.saveAll(jobCategory);
        return "create jobCategory success";
    }


    public List<JobCategoryResponse> getAllJobCategory() {
        List<JobCategoryResponse> jobs = new ArrayList<JobCategoryResponse>();
        for (JobCategory jobCategory : jobCategoryRepository.findAll()) {
            jobs.add(jobCategoryMapper.toJobCategoryResponse(jobCategory));
        }
        return jobs;
    }
}
