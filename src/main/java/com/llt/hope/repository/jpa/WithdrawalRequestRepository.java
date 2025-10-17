package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.entity.User;
import com.llt.hope.entity.WithdrawalRequest;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {

    Page<WithdrawalRequest> findByPostVolunteer(PostVolunteer postVolunteer, Pageable pageable);

    Page<WithdrawalRequest> findByUser(User user, Pageable pageable);

    Page<WithdrawalRequest> findByStatus(String status, Pageable pageable);

    @Query(
            "SELECT wr FROM WithdrawalRequest wr WHERE wr.postVolunteer.id = :postVolunteerId ORDER BY wr.requestedAt DESC")
    Page<WithdrawalRequest> findByPostVolunteerId(@Param("postVolunteerId") Long postVolunteerId, Pageable pageable);

    @Query(
            "SELECT wr FROM WithdrawalRequest wr WHERE wr.postVolunteer.id = :postVolunteerId AND wr.status = :status ORDER BY wr.requestedAt DESC")
    Page<WithdrawalRequest> findByPostVolunteerIdAndStatus(
            @Param("postVolunteerId") Long postVolunteerId, @Param("status") String status, Pageable pageable);

    @Query(
            "SELECT wr FROM WithdrawalRequest wr WHERE wr.postVolunteer.id = :postVolunteerId AND wr.status = 'APPROVED' ORDER BY wr.processedAt DESC")
    List<WithdrawalRequest> findLastApprovedWithdrawal(@Param("postVolunteerId") Long postVolunteerId);
}
