package com.llt.hope.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.PostVolunteer;

@Repository
public interface PostVolunteerRepository
        extends JpaRepository<PostVolunteer, Long>, JpaSpecificationExecutor<PostVolunteer> {
    Page<PostVolunteer> findPostVolunteerByIsActive(boolean isActive, Pageable pageable);

    Page<PostVolunteer> findPostVolunteerByStatus(String status, Pageable pageable);
}
