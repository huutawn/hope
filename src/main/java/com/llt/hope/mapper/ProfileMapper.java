package com.llt.hope.mapper;

import com.llt.hope.dto.request.ProfileCreationRequest;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest request);

    ProfileResponse toProfileResponse(Profile profile);
}
