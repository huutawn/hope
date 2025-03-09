package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.llt.hope.dto.request.UserCreationRequest;
import com.llt.hope.dto.request.UserUpdateRequest;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
