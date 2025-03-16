package com.llt.hope.service;


import com.llt.hope.dto.request.ProductCategoryCreationRequest;
import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.mapper.ProductCategoryMapper;
import com.llt.hope.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.JobCategoryCreationRequest;
import com.llt.hope.dto.response.JobCategoryResponse;
import com.llt.hope.entity.JobCategory;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.JobCategoryMapper;
import com.llt.hope.repository.JobCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductCategoryService {

    ProductCategoryRepository productCategoryRepository;
    ProductCategoryMapper productCategoryMapper;
    public ProductCategoryResponse createProductCategory(ProductCategoryCreationRequest request) {
        if (productCategoryRepository.existsProductCategoryByName(request.getName()))
            throw new AppException(ErrorCode.CATEGORY_HAS_EXISTED);
        ProductCategory productCategory = ProductCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return productCategoryMapper.toProductCategoryResponse(productCategoryRepository.save(productCategory));
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("ProductCategory with ID " + id + " does not exist.");
        }
        productCategoryRepository.deleteById(id);
    }

    public List<ProductCategory> getAllProductCategory(){
        return productCategoryRepository.findAll();
    }
}
