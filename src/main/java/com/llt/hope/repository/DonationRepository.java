package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Job;

@Repository
public interface DonationRepository extends JpaRepository<Job, String> {}
