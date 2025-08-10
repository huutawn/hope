package com.llt.hope.repository.jpa;

import java.time.LocalDateTime;

import com.llt.hope.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Job j WHERE j.applicationDeadline < :now")
    void deleteByDeadlineBefore(@Param("now") LocalDateTime now);

    Page<Job> findAllByCompany(Company company, Pageable pageable);
}
