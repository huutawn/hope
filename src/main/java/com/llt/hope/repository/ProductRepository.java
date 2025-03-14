package com.llt.hope.repository;

import com.llt.hope.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    Optional<Product> findProductByName(String name);

}
