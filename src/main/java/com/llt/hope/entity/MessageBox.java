package com.llt.hope.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    User receiver;

    @ManyToOne
    @JoinColumn(name = "container_id")
    MessageContainer container;
    
    @OneToMany(mappedBy = "messageBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages;
    
    private LocalDateTime lastMessageTime;
    
    private int unreadCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
