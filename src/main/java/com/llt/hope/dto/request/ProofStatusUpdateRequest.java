package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProofStatusUpdateRequest {
    private String status;
    private String reason; // Optional reason for rejection
}
