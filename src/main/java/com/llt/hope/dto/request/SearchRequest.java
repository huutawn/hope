package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    @NotBlank(message = "Keyword is required")
    @Size(min = 1, max = 255, message = "Keyword must be between 1 and 255 characters")
    String keyword;
    
    // Pagination
    @Builder.Default
    Integer page = 0;
    
    @Builder.Default
    Integer size = 20;
}
