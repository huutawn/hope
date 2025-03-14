package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
