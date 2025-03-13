package com.llt.hope.mapper;

import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory);
}
