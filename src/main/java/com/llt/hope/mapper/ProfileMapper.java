package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.ProfileCreationRequest;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest request);

    ProfileResponse toProfileResponse(Profile profile);
}
