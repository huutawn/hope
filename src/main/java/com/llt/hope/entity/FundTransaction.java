package com.llt.hope.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
// lưu trữ lịch sử từ thiện
public class FundTransaction {
    @Id
    Long id;
}
