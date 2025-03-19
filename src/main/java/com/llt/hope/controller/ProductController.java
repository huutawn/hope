package com.llt.hope.controller;

import java.util.List;

import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.Post;
import com.llt.hope.entity.Product;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    ProductService productService;

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<ProductResponse>> getAllProducts(
            @Filter Specification<Product> spec,

    @RequestParam(value = "page", required = false, defaultValue = "1") int page,

    @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getAllProduct(spec, page, size))
                .build();
    }

    @GetMapping("/getProduct")
    public ApiResponse<PageResponse<ProductResponse>> getProducts(
            @Filter Specification<Product> spec,

            @RequestParam(value = "page", required = false, defaultValue = "1") int page,

            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getAllProduct(spec, page, size))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductCreationRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PatchMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long productId, @RequestBody ProductUpdateRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId, request))
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<String>builder()
                .result("Product deleted successfully")
                .build();
    }
}
