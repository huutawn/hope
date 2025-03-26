package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.llt.hope.dto.request.SellerCreationRequest;
import com.llt.hope.dto.response.SellerResponse;
import com.llt.hope.entity.Seller;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    Seller toSeller(SellerCreationRequest request);

    Seller toSellerProfile(SellerCreationRequest request);
    
    SellerResponse toSellerProfileResponse(Seller seller);
}
