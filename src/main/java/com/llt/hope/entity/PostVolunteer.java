package com.llt.hope.entity;

import com.llt.hope.service.LikeService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String location;
    @Column(columnDefinition = "TEXT")
    private String content;
    private BigDecimal fund;
    private BigDecimal totalAmount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private boolean isActive;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MediaFile> files;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private User user;
    @OneToMany(mappedBy = "postVolunteer")
    private List<Support> supports;

}
