package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.JobCategory;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    Optional<JobCategory> findJobCategoryByName(String name);
    boolean existsJobCategoryByName(String name);
}
