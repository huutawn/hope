package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.request.SellerCreationRequest;
import com.llt.hope.dto.response.ActiveCompanyResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.SellerResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.SellerMapper;
import com.llt.hope.repository.jpa.*;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SellerService {
    SellerRepository sellerRepository;
    UserRepository userRepository;
    SellerMapper sellerMapper;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;

    @Transactional
    public SellerResponse createSeller(SellerCreationRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem user đã có hồ sơ seller chưa
        if (sellerRepository.findByUserId(user.getId()).isPresent()) {
            throw new AppException(ErrorCode.SELLER_PROFILE_ALREADY_EXISTS);
        }

        // Tạo SellerProfile (non-active)
        Seller seller = Seller.builder()
                .user(user)
                .phone(request.getPhone())
                .email(request.getEmail())
                .storeName(request.getStoreName())
                .storeDescription(request.getStoreDescription())
                .isActive(false) // Chưa được kích hoạt
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        seller = sellerRepository.save(seller);

        user.setSeller(seller);
        userRepository.save(user);

        SellerResponse sellerProfileResponse = SellerResponse.builder()
                .id(seller.getId())
                .email(seller.getEmail())
                .phone(seller.getPhone())
                .storeName(seller.getStoreName())
                .storeDescription(seller.getStoreDescription())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt())
                .isActive(seller.isActive())
                .build();

        log.info("Seller profile created for user: {}. Seller role assigned (pending activation).", user.getEmail());


        return sellerProfileResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ActiveCompanyResponse activeSeller(long Id) {
        log.info("seller id: " + Id);
        Optional<Seller> seller = sellerRepository.findById(Id);
        Seller seller1 = seller.get();
        if (seller1.isActive()) {
            throw new AppException(ErrorCode.SELLER_PROFILE_ALREADY_ACTIVE);
        }
        log.info("seller name" + seller1.getStoreName());
        seller.get().setActive(true);
        Optional<Profile> profile = profileRepository.findProfileBySeller(seller1);
        log.info("profile: " + profile.get().getId());
        Profile profile1;
        if (profile.isPresent()) {
            profile1 = profile.get();
        } else {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        seller1 = sellerRepository.save(seller1);
        profile1.setSeller(seller1);
        profileRepository.save(profile1);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefindRole.SELLER_ROLE).ifPresent(roles::add);
        User user = userRepository
                .findUserByProfile(profile1)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setRoles(roles);
        ActiveCompanyResponse activeCompanyResponse = ActiveCompanyResponse.builder()
                .id(seller1.getId())
                .isActive(seller1.isActive())
                .name(seller1.getStoreName())
                .build();
        return activeCompanyResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<SellerResponse> getAllSellerProfiles(boolean isActive, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Specification<Seller> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), isActive);

        Page<Seller> sellerProfiles = sellerRepository.findAll(spec, pageable);
        List<SellerResponse> sellerProfileResponses = sellerProfiles.getContent().stream()
                .map(sellerMapper::toSellerProfileResponse)
                .collect(Collectors.toList());

        return PageResponse.<SellerResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(sellerProfiles.getTotalElements())
                .totalPages(sellerProfiles.getTotalPages())
                .data(sellerProfileResponses)
                .build();
    }

    public void deleteSellerProfile(Long id) {
        if (!sellerRepository.existsById(id)) {
            throw new AppException(ErrorCode.SELLER_PROFILE_NOT_EXISTS);
        }
        sellerRepository.deleteById(id);
        log.info("Deleted seller profile with id: {}", id);
    }
}
