package com.llt.hope.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.GetMessageRequest;
import com.llt.hope.dto.request.SendMessageRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.service.MessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @PostMapping("/send")
    public ApiResponse<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.sendMessage(
                        request.getSenderEmail(), request.getReceiverEmail(), request.getContent()))
                .build();
    }

    @GetMapping
    public ApiResponse<List<MessageResponse>> getMessages(@RequestBody GetMessageRequest request) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getMessagesBetweenUsers(request.getUser1Email(), request.getUser2Email()))
                .build();
    }
}
