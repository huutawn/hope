package com.llt.hope.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
