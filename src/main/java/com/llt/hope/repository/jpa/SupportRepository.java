package com.llt.hope.repository.jpa;


import com.llt.hope.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    // Lấy danh sách những người đã ủng hộ cho một bài đăng cụ thể
    List<Support> findByPostVolunteerId(Long postVolunteerId);

    // Lấy danh sách bài đăng mà một người đã ủng hộ
    List<Support> findByUserId(String userId);
}
