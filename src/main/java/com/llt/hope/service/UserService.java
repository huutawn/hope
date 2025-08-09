package com.llt.hope.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import jakarta.mail.MessagingException;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.dto.request.*;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.dto.response.VerifiOTPResponse;
import com.llt.hope.entity.Profile;
import com.llt.hope.entity.Role;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.UserMapper;
import com.llt.hope.repository.jpa.RoleRepository;
import com.llt.hope.repository.jpa.UserRepository;

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
    ResendEmailService resendEmailService;

    public UserResponse createUser(UserCreationRequest request) {
        if (repository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        Role role=new Role();
        if(request.getRole()!=null){
        role=roleRepository.findById(request.getRole())
                        .orElseGet(()->roleRepository.findById(PredefindRole.USER_ROLE)
                                .orElseThrow(()->new AppException(ErrorCode.ROLE_NOT_EXISTED)));}
        else {
            role=roleRepository.findById(PredefindRole.USER_ROLE)
                    .orElseThrow(()->new AppException(ErrorCode.ROLE_NOT_EXISTED));
        }
        roles.add(role);
        user.setRoles(roles);
        user.setAccepted(true);
        repository.saveAndFlush(user);
        Profile profile =
                profileService.createInitProfile(request.getEmail(), request.getPhone(), request.getFullName());
        user.setProfile(profile);
        user.setAccepted(true);
        return userMapper.toUserResponse(repository.save(user));
        // hjhjhjhj
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

    public static String generateOtp() {

        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Transactional
    public void sendOtpForgotPassword(ForgotPasswordRequest request)
            throws MessagingException, UnsupportedEncodingException {
        log.info(request.getEmail());

        User user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String otp = generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        user.setOtp(otp);
        user.setOtpExpiryDate(expiryDate);

        String subject = "Your OTP Code";
        String content = String.format(
                "<p>Hello,</p>"
                        + "<p>We received a request to reset your password. Use the following OTP to reset it:</p>"
                        + "<h2>%s</h2>"
                        + "<p>If you did not request this, please ignore this email.</p>"
                        + "<p>Best regards,<br/>Your Company</p>",
                otp);
        resendEmailService.sendEmail(request.getEmail(), subject, content);
    }
    public UserResponse banUser(BannedReq req){
        User user=repository.findById(req.getUserId())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setAccepted(req.getIsBanned());
        UserResponse userResponse=UserResponse.builder()
                .id(user.getId())
                .fund(user.getFund())
                .phone(user.getPhone())
                .accepted(user.isAccepted())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .build();
        repository.save(user);
        return userResponse;

    }

    @Transactional
    public void resetPassword(PasswordCreationRequest request) {
        User user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        if (user.getOtpExpiryDate() == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(null);
        user.setOtpExpiryDate(null);
        repository.save(user);
    }

    public VerifiOTPResponse verifyOtp(VerifiOtpRequest request) {
        User user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            return VerifiOTPResponse.builder().valid(false).build();
        }

        if (user.getOtpExpiryDate() == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            return VerifiOTPResponse.builder().valid(false).build();
        }

        return VerifiOTPResponse.builder().valid(true).build();
    }
}
