package com.llt.hope.repository.jpa;

import java.util.Optional;

import com.llt.hope.entity.Post;
import com.llt.hope.entity.Seller;
import com.llt.hope.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.llt.hope.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findProductBySeller(User seller, Pageable pageable);
}
