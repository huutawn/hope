package com.llt.hope.service;


import com.llt.hope.dto.request.CVCreateReq;
import com.llt.hope.dto.request.CVUpdateReq;
import com.llt.hope.dto.response.CVResponse;
import com.llt.hope.dto.response.JobResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.entity.CVForm;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.CVMapper;
import com.llt.hope.repository.jpa.CVRepository;
import com.llt.hope.repository.jpa.UserRepository;
import com.llt.hope.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CVService {
    CVRepository cvRepository;
    CVMapper cvMapper;
    UserRepository userRepository;

    public CVResponse create(CVCreateReq req){
        User user=userRepository.findByEmail(SecurityUtils.getCurrentUserLogin()
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)))
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        CVForm cvForm=cvMapper.toCVForm(req);
        cvForm.setUser(user);
        cvForm=cvRepository.save(cvForm);
        return cvMapper.toCVResponse(cvForm);
    }
    public PageResponse<CVResponse> getAll(int page,int size){
        User user=userRepository.findByEmail(SecurityUtils.getCurrentUserLogin()
                        .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)))
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CVForm> cvForms=cvRepository.findAllByUser(user,pageable);
        List<CVResponse> cvResponses=cvForms.getContent().stream()
                .map(cvMapper::toCVResponse).collect(Collectors.toList());
        return PageResponse.<CVResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(cvForms.getTotalElements())
                .totalPages(cvForms.getTotalPages())
                .data(cvResponses)
                .build();
    }

    public CVResponse get(Long id){
        CVForm cvForm=cvRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.CV_NOT_FOUND));
        return cvMapper.toCVResponse(cvForm);
    }
    public CVResponse update(CVUpdateReq req){
        CVForm cvForm=cvRepository.findById(req.getCvId())
                .orElseThrow(()->new AppException(ErrorCode.CV_NOT_FOUND));
        cvForm=cvMapper.toCVForm(req);
         return cvMapper.toCVResponse(cvForm);
    }
}
