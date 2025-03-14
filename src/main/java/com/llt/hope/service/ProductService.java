package com.llt.hope.service;


import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProductMapper;
import com.llt.hope.repository.ProductCategoryRepository;
import com.llt.hope.repository.ProductRepository;
import com.llt.hope.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    ProductCategoryRepository productCategoryRepository;
    UserRepository userRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_HAS_EXISTED);
        }

        ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        User seller = userRepository.findById(request.getSeller_id())
                .orElseThrow(() -> new RuntimeException("Seller không tồn tại!"));

        Product product = productMapper.toProduct(request);
        product.setProductCategory(category);
        product.setSeller(seller);

        return productMapper.toProductResponse(productRepository.save(product));
    }
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProduct(){
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        productRepository.deleteById(productId);
    }
}
