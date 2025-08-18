package com.llt.hope.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    
    private String message;
    
    private String type;

    private boolean isRead;

    private LocalDateTime createdAt;
}
