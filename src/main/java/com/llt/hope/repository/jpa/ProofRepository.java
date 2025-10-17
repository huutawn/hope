package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.Proof;
import com.llt.hope.entity.User;

@Repository
public interface ProofRepository extends JpaRepository<Proof, Long> {
    Page<Proof> findProofByPostVolunteer(PostVolunteer postVolunteer, Pageable pageable);

    Page<Proof> findByStatus(String status, Pageable pageable);

    Page<Proof> findByUser(User user, Pageable pageable);

    Page<Proof> findByUserAndStatus(User user, String status, Pageable pageable);

    @Query("SELECT p FROM Proof p WHERE p.postVolunteer.id = :postVolunteerId")
    Page<Proof> findByPostVolunteerId(@Param("postVolunteerId") Long postVolunteerId, Pageable pageable);

    @Query("SELECT p FROM Proof p WHERE p.postVolunteer.id = :postVolunteerId AND p.status = :status")
    Page<Proof> findByPostVolunteerIdAndStatus(
            @Param("postVolunteerId") Long postVolunteerId, @Param("status") String status, Pageable pageable);

    List<Proof> findByStatus(String status);
}
