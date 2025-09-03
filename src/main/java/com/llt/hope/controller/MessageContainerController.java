package com.llt.hope.controller;

import com.llt.hope.dto.request.CommentCreationRequest;
import com.llt.hope.dto.request.CommentUpdateRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.CommentResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message-container")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageContainerController {
}
