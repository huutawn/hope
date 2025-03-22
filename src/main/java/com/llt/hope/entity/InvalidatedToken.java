package com.llt.hope.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
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
}
