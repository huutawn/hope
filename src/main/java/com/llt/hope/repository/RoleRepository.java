package com.llt.hope.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(String name);
}
