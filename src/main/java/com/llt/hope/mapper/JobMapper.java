package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.entity.Job;

@Mapper(componentModel = "spring")
public interface JobMapper {
    JobResponse toJobResponse(Job job);
}
