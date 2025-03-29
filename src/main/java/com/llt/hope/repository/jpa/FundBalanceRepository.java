package com.llt.hope.repository.jpa;

import com.llt.hope.entity.FundBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundBalanceRepository extends JpaRepository<FundBalance,Long> {
}
