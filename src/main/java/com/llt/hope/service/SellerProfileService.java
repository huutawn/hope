package com.llt.hope.service;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.request.SellerProfileCreationRequest;
import com.llt.hope.dto.response.SellerProfileResponse;
import com.llt.hope.entity.Role;
import com.llt.hope.entity.SellerProfile;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.SellerProfileMapper;
import com.llt.hope.repository.RoleRepository;
import com.llt.hope.repository.SellerProfileRepository;
import com.llt.hope.repository.UserRepository;
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
    public SellerProfileResponse createSeller(@Valid @RequestBody SellerProfileCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("Authenticated user: {}", email);

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem user đã có hồ sơ seller chưa
        sellerProfileRepository.findByUserId(user.getId()).ifPresent(profile -> {
            throw new AppException(ErrorCode.SELLER_PROFILE_ALREADY_EXISTS);
        });

        Role sellerRole = roleRepository
                .findRoleByName(PredefindRole.SELLER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Thêm role SELLER cho user
        user.getRoles().add(sellerRole);
        userRepository.save(user);

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
}
