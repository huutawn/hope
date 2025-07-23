package com.llt.hope.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailResponse {

    private String toEmail;

    private String subject;

    private String body;
}
