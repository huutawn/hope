package com.llt.hope.repository.jpa;

import com.llt.hope.entity.Order;
import com.llt.hope.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    void deleteByOrder(Order order);
}
