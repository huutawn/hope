package com.llt.hope.repository;

import com.llt.hope.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long>
{

    Optional<ProductCategory> findProductCategoryByName(String name);
    boolean existsProductCategoryByName(String name);
}
