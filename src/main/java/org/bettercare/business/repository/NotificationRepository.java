package org.bettercare.business.repository;

import org.bettercare.domain.model.Notification;
import org.bettercare.domain.model.UserAccount;

import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    void markSeen(int id);

    List<Notification> findByUser(UserAccount user);
    List<Notification> findAll();

    void markAllSeenForUser(int userId);

    List<Notification> findLatestByUser(int userId, int limit);
}