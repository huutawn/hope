package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {}
