package com.llt.hope.mapper;

import com.llt.hope.dto.response.ProfileResponse;
import com.llt.hope.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.llt.hope.dto.request.UserCreationRequest;
import com.llt.hope.dto.request.UserUpdateRequest;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
   public User toUser(UserCreationRequest request){
        return User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .build();

    }
   public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .roles(user.getRoles())
                .fund(user.getFund())
                .email(user.getEmail())
                .accepted(user.isAccepted())
                .phone(user.getPhone())
                .profile(toProfileResponse(user.getProfile()))
                .build();
    }
   public ProfileResponse toProfileResponse(Profile profile){
        return ProfileResponse.builder()
                .dob(profile.getDob())
                .id(profile.getId())
                .bio(profile.getBio())
                .city(profile.getCity())
                .address(profile.getAddress())
                .country(profile.getCountry())
                .profilePicture(profile.getProfilePicture())
                .disabilityType(profile.getDisabilityType())
                .fullName(profile.getFullName())
                .gender(profile.getGender())
                .phone(profile.getPhone())
                .build();
    }
    public User updateUser(User user,UserUpdateRequest request){
       user.getProfile().setFullName(request.getLastName()+request.getFirstName());
       user.getProfile().setDob(request.getDob());
       return user;
    }
}
