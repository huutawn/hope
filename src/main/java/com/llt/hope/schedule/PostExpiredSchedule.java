package com.llt.hope.schedule;

import java.time.LocalDate;
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
        List<PostVolunteer> postVolunteers=postVolunteerRepository.findAllByStatus(StatusCons.NORMAL);
        for(PostVolunteer postVolunteer:postVolunteers){
            if(postVolunteer.getExpiryDate().isEqual(LocalDate.now())||postVolunteer.getExpiryDate().isAfter(LocalDate.now())
            || postVolunteer.getTotalAmount().equals(postVolunteer.getFund())
            ) {
                postVolunteer.setStatus(StatusCons.FULLED);
                postVolunteerRepository.save(postVolunteer);
            }
        }
    }
}
