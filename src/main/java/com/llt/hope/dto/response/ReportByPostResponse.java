package com.llt.hope.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportByPostResponse {
    private long reportId;
    private String userId;
    private long postId;
    private String content;
}
