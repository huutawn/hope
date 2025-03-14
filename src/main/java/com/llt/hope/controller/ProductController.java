package com.llt.hope.controller;

import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProduct())
                .build();
    }

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreationRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping("/{productId}")
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
