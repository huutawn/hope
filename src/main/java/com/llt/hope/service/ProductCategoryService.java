package com.llt.hope.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.ProductCategoryCreationRequest;
import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProductCategoryMapper;
import com.llt.hope.repository.jpa.ProductCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        productCategoryRepository.deleteById(id);
    }
    public ProductCategory getCategory( Long id){
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
    public List<ProductCategory> getAllProductCategory() {
        return productCategoryRepository.findAll();
    }
}
