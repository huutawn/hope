package com.llt.hope.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "invalidated_tokens",
        indexes = {@Index(name = "idx_expiry_time", columnList = "expiry_date_time")})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvalidatedToken {
    @Id
    private String id;

    @Column(nullable = false)
    private Date expiryTime;

    @Column(name = "expiry_date_time")
    private LocalDateTime expiryDateTime;
}
