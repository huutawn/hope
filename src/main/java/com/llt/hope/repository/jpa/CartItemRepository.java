package com.llt.hope.repository.jpa;

import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Product;
import com.llt.hope.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
