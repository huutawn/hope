package com.llt.hope.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.llt.hope.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
