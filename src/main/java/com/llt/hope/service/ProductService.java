package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.ProductMapper;
import com.llt.hope.repository.jpa.MediaFileRepository;
import com.llt.hope.repository.jpa.ProductCategoryRepository;
import com.llt.hope.repository.jpa.ProductRepository;
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
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    MediaFileRepository mediaFileRepository;
    ProductCategoryRepository productCategoryRepository;

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request, Authentication authentication) {

        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Set<MediaFile> mediaFiles = new HashSet<>();
        ProductCategory category = productCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        User seller = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!request.getImagesFile().isEmpty() || request.getImagesFile() != null) {
            try {
                List<MediaFile> uploadedFiles = new ArrayList<>();
                for (MultipartFile file : request.getImagesFile()) {
                    MediaFile mediaFile = cloudinaryService.uploadFile(file, "product", seller.getEmail());
                    mediaFiles.add(mediaFile);
                }
                mediaFiles.addAll(mediaFileRepository.saveAll(uploadedFiles));
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
            }
        }
        Product product = Product.builder()
                .createdAt(LocalDateTime.now())
                .productCategory(category)
                .seller(seller)
                .name(request.getName())
                .images(mediaFiles)
                .price(request.getPrice())
                .description(request.getDescription())
                .infomation(request.getInfomation())
                .dimensions(request.getDimensions())
                .inventory(request.getInventory())
                .build();

        product = productRepository.save(product);
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .productCategory(category)
                .createdAt(product.getCreatedAt())
                .seller(seller)
                .name(product.getName())
                .images(product.getImages())
                .price(product.getPrice())
                .description(product.getDescription())
                .infomation(product.getInfomation())
                .dimensions(product.getDimensions())
                .inventory(product.getInventory())
                .build();
        return productResponse;
    }

    public PageResponse<ProductResponse> getAllProduct(Specification<Product> spec, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = products.getContent().stream()
                .map(productMapper::toProductResponse)
                .toList();
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .data(productResponses)
                .build();
    }

    public ProductResponse getProduct(Long id) {
        return productMapper.toProductResponse(
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        productRepository.deleteById(productId);
    }

    @PreAuthorize("hasRole('SELLER')")
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productMapper.updateProduct(product, request);

        return productMapper.toProductResponse(productRepository.save(product));
    }
}
