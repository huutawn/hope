package com.llt.hope.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    String keyword;
    Integer totalResults;
    Integer currentPage;
    Integer totalPages;
    Integer pageSize;

    List<JobResponse> jobs;
    List<PostResponse> posts;
    List<PostVolunteerResponse> postVolunteers;

    Integer jobCount;
    Integer postCount;
    Integer postVolunteerCount;
}
