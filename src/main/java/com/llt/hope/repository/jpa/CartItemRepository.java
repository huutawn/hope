package com.llt.hope.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.CartItem;
import com.llt.hope.entity.Order;
import com.llt.hope.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndOrder(User user, Order order);
}
