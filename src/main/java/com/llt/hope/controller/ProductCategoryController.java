package com.llt.hope.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ProductCategoryCreationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ProductCategoryResponse;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.service.ProductCategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/productCategory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCategoryController {
    ProductCategoryService productCategoryService;

    @PostMapping
    public ApiResponse<ProductCategoryResponse> createCategory(@RequestBody ProductCategoryCreationRequest request) {
        return ApiResponse.<ProductCategoryResponse>builder()
                .result(productCategoryService.createProductCategory(request))
                .build();
    }

    @GetMapping
    public List<ProductCategory> getAllCategories() {
        return productCategoryService.getAllProductCategory();
    }
}
