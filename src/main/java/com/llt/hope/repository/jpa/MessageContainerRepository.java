package com.llt.hope.repository.jpa;

import com.llt.hope.entity.Comment;
import com.llt.hope.entity.MessageBox;
import com.llt.hope.entity.MessageContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageContainerRepository extends JpaRepository<MessageContainer, Long>, JpaSpecificationExecutor<MessageContainer> {
}
