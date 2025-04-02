package com.llt.hope.service;

import com.llt.hope.dto.response.PostVolunteerResponse;
import com.llt.hope.dto.response.SupportResponse;
import com.llt.hope.dto.response.UserResponse;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.Support;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.PostVolunteerMapper;
import com.llt.hope.mapper.SupportMapper;
import com.llt.hope.mapper.UserMapper;
import com.llt.hope.repository.jpa.SupportRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SupportService {
    SupportRepository supportRepository;
    SupportMapper supportMapper;
    UserRepository userRepository;

    public List<SupportResponse> getSupportersByPostVolunteer(Long postVolunteerId) {
        return supportRepository.findByPostVolunteerId(postVolunteerId)
                .stream()
                .map(supportMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SupportResponse> getDonatedPostsByCurrentUser() {
        String email= SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return supportRepository.findByUserId(user.getId())
                .stream()
                .map(supportMapper::toResponse)
                .collect(Collectors.toList());
    }
}
