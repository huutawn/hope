package com.llt.hope.dto.response;

import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostVolunteerResponse {
    private Long id;
    private String title;
    private String location;
    private String content;
    private BigDecimal fund;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isActive;
    private List<MediaFile> files;
    private UserResponse user;
}
