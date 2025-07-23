package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.llt.hope.dto.request.ReportRequest;
import com.llt.hope.dto.response.ReportByPostResponse;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.Report;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.PostVolunteerRepository;
import com.llt.hope.repository.jpa.ReportRepository;
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
public class ReportService {
    ReportRepository reportRepository;
    UserRepository userRepository;
    PostVolunteerRepository postVolunteerRepository;

    public ReportByPostResponse report(ReportRequest request) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Report report = Report.builder()
                .content(request.getContent())
                .postVolunteer(postVolunteer)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        report = reportRepository.save(report);
        return ReportByPostResponse.builder()
                .reportId(report.getId())
                .postId(report.getPostVolunteer().getId())
                .userId(report.getUser().getId())
                .content(report.getContent())
                .build();
    }

    public List<ReportByPostResponse> getAllReportsByPost(long postId) {
        PostVolunteer postVolunteer = postVolunteerRepository
                .findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        List<Report> reports = reportRepository.findAllReportByPostVolunteerId(postId);
        List<ReportByPostResponse> reportByPostResponses = new ArrayList<>();
        for (Report report : reports) {
            reportByPostResponses.add(ReportByPostResponse.builder()
                    .reportId(report.getId())
                    .postId(report.getPostVolunteer().getId())
                    .userId(report.getUser().getId())
                    .content(report.getContent())
                    .build());
        }
        return reportByPostResponses;
    }
}
