package com.llt.hope.mapper;

import com.llt.hope.dto.request.SellerCreationRequest;

import com.llt.hope.dto.response.SellerResponse;
import com.llt.hope.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    Seller toSeller(SellerCreationRequest request);

    Seller toSellerProfile(SellerCreationRequest request);
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    SellerResponse toSellerProfileResponse(Seller seller);
}
