package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.llt.hope.entity.MediaFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostVolunteerResponse {
    private Long id;
    private String userId;
    private String name;
    private String userPic;
    private String title;
    private String location;
    private String content;
    private BigDecimal fund;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String stk;
    private String bankName;
    private boolean isActive;
    private List<MediaFile> files;
    
    // Comment information
    private long commentCount;

}
