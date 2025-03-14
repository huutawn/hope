package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.CompanyResponse;
import com.llt.hope.entity.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toCompanyResponse(Company company);
}
