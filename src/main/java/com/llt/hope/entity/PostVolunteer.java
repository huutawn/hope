package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

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

    private String stk;
    private String bankName;

    private BigDecimal fund;
    private BigDecimal totalAmount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> files;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postVolunteer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Report> reports;

    @OneToMany(mappedBy = "postVolunteer")
    private List<Support> supports;
}
