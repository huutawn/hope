package com.llt.hope.repository.jpa;

import java.util.List;
import java.util.Optional;

import com.llt.hope.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>, JpaSpecificationExecutor<Seller> {
    Optional<Seller> findByUserId(String userId);
}
