package com.llt.hope.entity;

import jakarta.persistence.Entity;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobCategory extends AbstractEntity<Long> {
    private String name;
    private String description;
}
