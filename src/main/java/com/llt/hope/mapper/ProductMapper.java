package com.llt.hope.mapper;


import com.llt.hope.dto.request.ProductCreationRequest;
import com.llt.hope.dto.request.ProductUpdateRequest;
import com.llt.hope.dto.response.ProductResponse;
import com.llt.hope.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toProduct(ProductCreationRequest productCreationRequest);
    @Mapping(source = "productCategory.id", target = "categoryId")
    @Mapping(source = "productCategory.name", target = "category")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "createdAt", target = "createdAt")
    ProductResponse toProductResponse(Product product);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
