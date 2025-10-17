package com.llt.hope.repository.jpa;


import com.llt.hope.entity.MessageContainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;


@Repository
public interface MessageContainerRepository extends JpaRepository<MessageContainer, Long>, JpaSpecificationExecutor<MessageContainer> {
}
