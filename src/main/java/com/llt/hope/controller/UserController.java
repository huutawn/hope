package com.llt.hope.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.*;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.dto.response.VerifiOTPResponse;
import com.llt.hope.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PatchMapping("/ban")
    ApiResponse<UserResponse> banUser(@RequestBody BannedReq req ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.banUser(req))
                .build();
    }
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @PostMapping("/send-otp")
    ApiResponse<Void> sendOtpForgotPassword(@RequestBody ForgotPasswordRequest request)
            throws MessagingException, UnsupportedEncodingException {

        userService.sendOtpForgotPassword(request);

        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Send Otp Successfully")
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<VerifiOTPResponse> verifyOtp(@RequestBody VerifiOtpRequest request) {
        var result = userService.verifyOtp(request);

        return ApiResponse.<VerifiOTPResponse>builder()
                .code(1000)
                .message("Verify Otp Successfully")
                .result(result)
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<?> resetPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Reset Password Successfully")
                .build();
    }
}
