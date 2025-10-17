package com.llt.hope.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Proof {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany
    private Set<MediaFile> proofImage;

    @ManyToOne
    @JsonBackReference
    private PostVolunteer postVolunteer;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
