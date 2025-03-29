package com.llt.hope.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.entity.Company;
import com.llt.hope.entity.Profile;
import com.llt.hope.entity.Seller;
import com.llt.hope.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findProfileByUser(User user);

    Optional<Profile> findProfileByCompany(Company company);

    Optional<Profile> findProfileBySeller(Seller seller);

    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.company = NULL WHERE p.company.id = :companyId")
    void unlinkProfilesFromCompany(@Param("companyId") Long companyId);

    void deleteBySellerId(Long sellerId);
}
