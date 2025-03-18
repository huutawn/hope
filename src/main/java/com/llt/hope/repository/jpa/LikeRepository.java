package com.llt.hope.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {}
