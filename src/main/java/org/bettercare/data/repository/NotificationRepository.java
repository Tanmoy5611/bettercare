package org.bettercare.data.repository;

import org.bettercare.business.entities.Notification;
import org.bettercare.business.entities.UserAccount;

import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    void markSeen(int id);

    List<Notification> findByUser(UserAccount user);
    List<Notification> findAll();

    void markAllSeenForUser(int userId);

    List<Notification> findLatestByUser(int userId, int limit);
}