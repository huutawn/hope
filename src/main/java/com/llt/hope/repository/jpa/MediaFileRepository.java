package com.llt.hope.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.MediaFile;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {}
