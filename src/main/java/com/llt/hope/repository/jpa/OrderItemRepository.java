package com.llt.hope.repository.jpa;

import com.llt.hope.entity.Order;
import com.llt.hope.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderId(Order orderId);

}
