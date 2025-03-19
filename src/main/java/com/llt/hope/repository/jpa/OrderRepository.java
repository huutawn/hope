package com.llt.hope.repository.jpa;

import com.llt.hope.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.llt.hope.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
