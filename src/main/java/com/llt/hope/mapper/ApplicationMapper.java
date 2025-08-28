package com.llt.hope.mapper;

import com.llt.hope.dto.response.JobApplicationResponse;
import com.llt.hope.dto.response.SupportResponse;
import com.llt.hope.entity.JobApplication;
import com.llt.hope.entity.Support;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {
   private final JobHandlerMapper jobHandlerMapper;
   private final UserMapper userMapper;

   public JobApplicationResponse toJobApplicationResponse(JobApplication jobApplication){
       return JobApplicationResponse.builder()
               .job(jobHandlerMapper.toJobResponse(jobApplication.getJob()))
               .id(jobApplication.getId())
               .cvForm(jobHandlerMapper.toCVFormResponse(jobApplication.getCvForm()))
               .applicant(userMapper.toUserResponse(jobApplication.getApplicant()))
               .build();
   }
}
