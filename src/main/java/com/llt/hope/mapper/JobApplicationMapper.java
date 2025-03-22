package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.JobApplicationResponse;
import com.llt.hope.entity.JobApplication;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {
    JobApplicationResponse toJobApplicationResponse(JobApplication application);
}
