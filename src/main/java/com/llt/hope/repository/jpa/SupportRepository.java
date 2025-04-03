package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Support;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    // Lấy danh sách những người đã ủng hộ cho một bài đăng cụ thể
    List<Support> findByPostVolunteerId(Long postVolunteerId);

    // Lấy danh sách bài đăng mà một người đã ủng hộ
    List<Support> findByUserId(String userId);
}
