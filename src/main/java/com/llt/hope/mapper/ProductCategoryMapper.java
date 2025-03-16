package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory);
}
