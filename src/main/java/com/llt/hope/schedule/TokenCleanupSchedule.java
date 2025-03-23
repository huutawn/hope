package com.llt.hope.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.llt.hope.entity.InvalidatedToken;
import com.llt.hope.repository.jpa.InvalidTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenCleanupSchedule {
    private final InvalidTokenRepository invalidTokenRepository;

    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ
    public void cleanupExpiredTokens() {
        System.out.println("Start Scheduler Delete Token Expired");
        List<InvalidatedToken> invalidatedTokens = invalidTokenRepository.findAll();
        if (!invalidatedTokens.isEmpty()) {
            invalidTokenRepository.deleteAll();
        }
    }
}
