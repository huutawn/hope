package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Notification;
import com.llt.hope.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find all notifications for a user ordered by creation date descending
     */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Find unread notifications for a user
     */
    List<Notification> findByUserAndIsReadFalse(User user);
    
    /**
     * Count unread notifications for a user
     */
    long countByUserAndIsReadFalse(User user);
    
    /**
     * Find notifications by type for a user
     */
    List<Notification> findByUserAndType(User user, String type);
    
    /**
     * Find notifications by user email
     */
    @Query("SELECT n FROM Notification n WHERE n.user.email = :userEmail ORDER BY n.createdAt DESC")
    List<Notification> findByUserEmail(@Param("userEmail") String userEmail);
    
    /**
     * Count unread notifications by user email
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.email = :userEmail AND n.isRead = false")
    long countUnreadByUserEmail(@Param("userEmail") String userEmail);
    
    /**
     * Delete all notifications for a user
     */
    void deleteByUser(User user);
    
    /**
     * Find recent notifications (within specified hours)
     */
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.createdAt >= CURRENT_TIMESTAMP - :hours HOUR ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotifications(@Param("user") User user, @Param("hours") int hours);
}
