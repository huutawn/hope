package com.llt.hope.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.InvalidatedToken;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidatedToken, String> {}
