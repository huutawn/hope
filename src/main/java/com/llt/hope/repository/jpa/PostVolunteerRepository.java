package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.User;

@Repository
public interface PostVolunteerRepository
        extends JpaRepository<PostVolunteer, Long>, JpaSpecificationExecutor<PostVolunteer> {
    Page<PostVolunteer> findPostVolunteerByIsActiveAndStatus(boolean isActive, String status, Pageable pageable);

    Page<PostVolunteer> findPostVolunteerByStatus(String status, Pageable pageable);

    List<PostVolunteer> findAllByStatus(String status);

    List<PostVolunteer> findPostVolunteerByUserAndStatus(User user, String status);
}
