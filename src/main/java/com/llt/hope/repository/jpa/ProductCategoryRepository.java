package com.llt.hope.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.llt.hope.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findProductCategoryByName(String name);

    boolean existsProductCategoryByName(String name);
}
