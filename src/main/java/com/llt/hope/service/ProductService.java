package com.llt.hope.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.llt.hope.repository.MediaFileRepository;
import com.llt.hope.repository.ProductCategoryRepository;
import com.llt.hope.repository.ProductRepository;
import com.llt.hope.repository.UserRepository;

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

        Set<MediaFile> mediaFiles = new HashSet<>();

        ProductCategory category = productCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        User seller = userRepository
                .findById(request.getSeller_id())
                .orElseThrow(() -> new RuntimeException("Seller không tồn tại!"));
        if(!request.getImagesFile().isEmpty()||request.getImagesFile()!=null){
        	try {


        		for(MultipartFile file:request.getImagesFile()){
        			MediaFile mediaFile = cloudinaryService.uploadFile(file,"product",seller.getEmail());
        			mediaFileRepository.saveAndFlush(mediaFile);
        			mediaFiles.add(mediaFile);
        		}
        User seller = userRepository
                .findById(request.getSeller_id())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!request.getImagesFile().isEmpty() || request.getImagesFile() != null) {
            try {
                List<MediaFile> upLoadFiles = new ArrayList<>();
                for (MultipartFile file : request.getImagesFile()) {
                    MediaFile mediaFile = cloudinaryService.uploadFile(file, "product", seller.getId());

                    mediaFiles.add(mediaFile);
                }
                mediaFiles.addAll(mediaFileRepository.saveAll(upLoadFiles));

        	} catch (IOException e) {
        		throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
        	}
        }
        Product product = Product.builder()
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
        //        product.setProductCategory(category);

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

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        productRepository.deleteById(productId);
    }
}
