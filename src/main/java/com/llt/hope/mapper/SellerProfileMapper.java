package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.SellerProfileCreationRequest;
import com.llt.hope.dto.response.SellerProfileResponse;
import com.llt.hope.entity.SellerProfile;

@Mapper(componentModel = "spring")
public interface SellerProfileMapper {

    SellerProfile toSellerProfile(SellerProfileCreationRequest request);

    SellerProfileResponse toSellerPRofileResponse(SellerProfile sellerProfile);
}
