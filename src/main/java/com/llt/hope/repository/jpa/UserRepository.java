package com.llt.hope.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Profile;
import com.llt.hope.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByProfile(Profile profile);
}
