package com.llt.hope.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.SellerProfile;

@Repository
public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    Boolean existsByEmail(String email);

    List<SellerProfile> findByActiveFalse();

    Optional<SellerProfile> findByUserId(String seller_id);
}
