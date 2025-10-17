package com.llt.hope.entity;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    User user;

    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<MessageBox> messageBoxes;
}
