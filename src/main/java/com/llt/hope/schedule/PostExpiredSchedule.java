package com.llt.hope.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.llt.hope.constant.StatusCons;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.repository.jpa.PostVolunteerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostExpiredSchedule {

    private final PostVolunteerRepository postVolunteerRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void expirePost() {

        List<PostVolunteer> postVolunteersExpired = postVolunteerRepository.findAll().stream()
                .filter(postVolunteer -> postVolunteer.getCreateAt() != null)
                .filter(postVolunteer -> postVolunteer.getCreateAt().plusDays(7).isBefore(LocalDateTime.now()))
                .toList();

        if (!postVolunteersExpired.isEmpty()) {
            postVolunteersExpired.forEach(post -> post.setStatus(StatusCons.EXPIRED));
            postVolunteerRepository.saveAll(postVolunteersExpired);
        }
    }
}
