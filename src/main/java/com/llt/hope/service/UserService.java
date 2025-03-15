package com.llt.hope.service;

import java.util.HashSet;
import java.util.List;

import com.llt.hope.entity.Profile;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.request.UserCreationRequest;
import com.llt.hope.dto.request.UserUpdateRequest;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.entity.Role;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.UserMapper;
import com.llt.hope.repository.RoleRepository;
import com.llt.hope.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository repository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileService profileService;

    public UserResponse createUser(UserCreationRequest request) {
        if (repository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefindRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        repository.saveAndFlush(user);
        Profile profile=profileService.createInitProfile(request.getEmail(), request.getPhone(), request.getFullName());
        user.setProfile(profile);
        return userMapper.toUserResponse(repository.save(user));
        //hjhjhjhj
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(repository.save(user));
    }

    public void deleteUser(String id) {
        User user = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        repository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return repository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
