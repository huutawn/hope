package com.llt.hope.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.ProductCategoryCreationRequest;
import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProductCategoryMapper;
import com.llt.hope.repository.jpa.ProductCategoryRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;

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
    UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ProductCategoryResponse createProductCategory(ProductCategoryCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (productCategoryRepository.existsProductCategoryByName(request.getName()))
            throw new AppException(ErrorCode.CATEGORY_HAS_EXISTED);
        ProductCategory productCategory = ProductCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return productCategoryMapper.toProductCategoryResponse(productCategoryRepository.save(productCategory));
    }
    // gigig
    @Transactional
    public void deleteCategoryById(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        productCategoryRepository.deleteById(id);
    }

    public ProductCategory getCategory(Long id) {
        return productCategoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<ProductCategory> getAllProductCategory() {
        return productCategoryRepository.findAll();
    }
}
