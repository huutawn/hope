/*
package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.request.SellerProfileCreationRequest;
import com.llt.hope.dto.response.SellerProfileResponse;
import com.llt.hope.entity.SellerProfile;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.SellerProfileMapper;
import com.llt.hope.repository.jpa.RoleRepository;
import com.llt.hope.repository.jpa.SellerProfileRepository;
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
public class SellerProfileService {
    SellerProfileRepository sellerProfileRepository;
    UserRepository userRepository;
    SellerProfileMapper sellerProfileMapper;
    RoleRepository roleRepository;

    @Transactional
    public SellerProfileResponse createSeller(SellerProfileCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("Authenticated user: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // Kiểm tra xem user đã có hồ sơ seller chưa
        sellerProfileRepository.findByUserId(user.getId()).ifPresent(profile -> {
            throw new AppException(ErrorCode.SELLER_PROFILE_ALREADY_EXISTS);
        });
        // Thêm role SELLER cho user
        HashSet<Role> roles = new HashSet<>();
        if (!user.getRoles().contains(roles)) {
            roleRepository.findById(PredefindRole.SELLER_ROLE).ifPresent(roles::add);
            userRepository.save(user);
        }
        SellerProfile sellerProfile = SellerProfile.builder()
                .user(user)
                .phone(request.getPhone())
                .email(request.getEmail())
                .storeName(request.getStoreName())
                .storeDescription(request.getStoreDescription())
                .active(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SellerProfile savedProfile = sellerProfileRepository.save(sellerProfile);
        return sellerProfileMapper.toSellerPRofileResponse(savedProfile);
    }
    */
/*public UserResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }*//*


}
*/
