package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.ProductCategory;
import com.llt.hope.entity.User;
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
    ProductCategoryRepository productCategoryRepository;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    MediaFileRepository mediaFileRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request) {

        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Set<MediaFile> mediaFiles = new HashSet<>();

        ProductCategory category = productCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        User seller = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!request.getImagesFile().isEmpty() || request.getImagesFile() != null) {
            try {

                for (MultipartFile file : request.getImagesFile()) {
                    MediaFile mediaFile = cloudinaryService.uploadFile(file, "product", seller.getEmail());
                    mediaFileRepository.saveAndFlush(mediaFile);
                    mediaFiles.add(mediaFile);
                }
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
            }
        }
        Product product = Product.builder()
                .createdAt(LocalDateTime.now())
                .seller(seller)
                .name(request.getName())
                .images(mediaFiles)
                .price(request.getPrice())
                .description(request.getDescription())
                .productCategory(category)
                .weight(request.getWeight())
                .dimensions(request.getDimensions())
                .creationProcess(request.getCreationProcess())
                .materialsUsed(request.getMaterialsUsed())
                .inventory(request.getInventory())
                .build();

        product = productRepository.save(product);
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .createdAt(product.getCreatedAt())
                .seller(seller)
                .name(product.getName())
                .images(product.getImages())
                .price(product.getPrice())
                .description(product.getDescription())
                .productCategory(category)
                .inventory(product.getInventory())
                .build();
        return productResponse;
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProduct() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
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
}
