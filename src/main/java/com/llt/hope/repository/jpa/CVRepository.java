package com.llt.hope.repository.jpa;

import com.llt.hope.entity.CVForm;
import com.llt.hope.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CVRepository extends JpaRepository<CVForm,Long> {
    Page<CVForm> findAllByUser(User user, Pageable pageable);
}
