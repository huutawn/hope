package com.llt.hope.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.llt.hope.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender.email = :user1Email AND m.receiver.email = :user2Email) "
            + "OR (m.sender.email = :user2Email AND m.receiver.email = :user1Email) ORDER BY m.sentAt ASC")
    List<Message> findMessagesBetweenUsers(
            @Param("user1Email") String user1Email, @Param("user2Email") String user2Email);

    @Query(
            "SELECT m FROM Message m WHERE m.sender.email = :sender AND m.receiver.email = :receiver AND m.isRead = false")
    List<Message> findUnreadMessages(@Param("sender") String sender, @Param("receiver") String receiver);
}
