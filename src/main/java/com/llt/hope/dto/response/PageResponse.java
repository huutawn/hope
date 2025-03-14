package com.llt.hope.dto.response;

import java.util.Collections;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    int currentPage; // trang hiện tại
    int totalPages; // tổng số trang
    int pageSize; // size của trang là bao nhiêu
    long totalElements; // tổng số phần tử, element

    @Builder.Default
    List<T> data = Collections.emptyList();
}
