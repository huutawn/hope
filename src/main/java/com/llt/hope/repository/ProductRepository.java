package com.llt.hope.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.llt.hope.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    Optional<Product> findProductByName(String name);
}
