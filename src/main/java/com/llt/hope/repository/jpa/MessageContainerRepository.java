package com.llt.hope.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.MessageContainer;

@Repository
public interface MessageContainerRepository
        extends JpaRepository<MessageContainer, Long>, JpaSpecificationExecutor<MessageContainer> {}
