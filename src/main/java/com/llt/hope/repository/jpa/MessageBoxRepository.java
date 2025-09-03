package com.llt.hope.repository.jpa;

import com.llt.hope.entity.MessageBox;
import com.llt.hope.entity.MessageContainer;
import com.llt.hope.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageBoxRepository extends JpaRepository<MessageBox, Long>, JpaSpecificationExecutor<MessageBox> {
    @Query("SELECT mb FROM MessageBox mb join mb.container c where c.user = :currentUser and mb.receiver = :receiver")
    Optional<MessageBox> findMessageBoxByReceiver(@Param("receiver")User receiver, @Param("currentUser") User currentUser);

    @Query("SELECT mb FROM MessageBox mb join mb.container c where c.user = :user ORDER BY mb.lastMessageTime DESC")
    List<MessageBox> findAllByUserOrderByLastMessageTimeDesc(@Param("user") User user);
    
    @Query("SELECT mb FROM MessageBox mb join mb.container c where c.user = :user ORDER BY mb.updatedAt DESC")
    List<MessageBox> findAllByUserOrderByUpdatedAtDesc(@Param("user") User user);
    
    @Query("SELECT COUNT(mb) FROM MessageBox mb join mb.container c where c.user = :user and mb.unreadCount > 0")
    long countUnreadMessageBoxesByUser(@Param("user") User user);
    
    @Query("SELECT SUM(mb.unreadCount) FROM MessageBox mb join mb.container c where c.user = :user")
    Long getTotalUnreadCountByUser(@Param("user") User user);
}
