package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.JobCategory;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {}
