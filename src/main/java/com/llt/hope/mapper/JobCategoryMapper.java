package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.entity.JobCategory;

@Mapper(componentModel = "spring")
public interface JobCategoryMapper {
    JobCategoryResponse toJobCategoryResponse(JobCategory jobCategory);
}
