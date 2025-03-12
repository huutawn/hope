package com.llt.hope.mapper;

import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.JobCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobCategoryMapper {
    JobCategoryResponse toJobCategoryResponse(JobCategory jobCategory);
}
