package com.llt.hope.repository;

import com.llt.hope.entity.Profile;
import com.llt.hope.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findProfileByUser(User user);
}
