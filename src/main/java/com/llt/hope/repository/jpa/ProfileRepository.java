package com.llt.hope.repository.jpa;

import java.util.Optional;

import com.llt.hope.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Company;
import com.llt.hope.entity.Profile;
import com.llt.hope.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findProfileByUser(User user);

    Optional<Profile> findProfileByCompany(Company company);

    Optional<Profile> findProfileBySeller(Seller seller);
}
